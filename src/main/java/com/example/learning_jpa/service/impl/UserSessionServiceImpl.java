package com.example.learning_jpa.service.impl;

import com.example.learning_jpa.entity.User;
import com.example.learning_jpa.entity.UserSession;
import com.example.learning_jpa.enums.Roles;
import com.example.learning_jpa.repository.UserSessionRepository;
import com.example.learning_jpa.service.UserSessionService;
import com.example.learning_jpa.util.AccessJwtUtil;
import com.example.learning_jpa.util.RefreshJwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserSessionServiceImpl implements UserSessionService {

    @Autowired
    private UserSessionRepository sessionRepository;

    @Autowired
    private AccessJwtUtil accessJwtUtil;

    @Autowired
    private RefreshJwtUtil refreshJwtUtil;

    @Value("${ACCESS_TOKEN_EXPIRATION_Time}")
    private int accessTokenExpiryTime;

    @Value("${REFRESH_TOKEN_EXPIATION_TIME}")
    private int refreshTokenExpiryTime;

    /**
     * Check if employee already has an active session
     */
    public boolean hasActiveSession(User user) {
        if (user.getRoles() == Roles.EMPLOYEE) {
            return sessionRepository.existsByUserAndIsActiveTrue(user);
        }
        return false; // Vendors can have multiple sessions
    }

    /**
     * Create a new session for user
     */
    @Transactional
    public UserSession createSession(User user, HttpServletRequest request) {
        String jti = accessJwtUtil.generateJti();
        String accessToken = accessJwtUtil.generateToken(user.getEmail(), user.getRoles(), jti);
        String refreshToken = refreshJwtUtil.generateRefreshToken(user.getEmail(), jti);

        // If employee, deactivate all previous sessions (single device restriction)
        if (user.getRoles() == Roles.EMPLOYEE) {
            sessionRepository.deactivateAllUserSessions(user);
            log.info("Deactivated all previous sessions for employee: {}", user.getEmail());
        }

        UserSession session = new UserSession();
        session.setUser(user);
        session.setJti(jti);
        session.setRefreshToken(refreshToken);

        Date accessExpiry = new Date(System.currentTimeMillis() + accessTokenExpiryTime);
        Date refreshExpiry = new Date(System.currentTimeMillis() + refreshTokenExpiryTime);

        session.setAccessTokenExpiry(LocalDateTime.ofInstant(accessExpiry.toInstant(), ZoneId.systemDefault()));
        session.setRefreshTokenExpiry(LocalDateTime.ofInstant(refreshExpiry.toInstant(), ZoneId.systemDefault()));
        session.setDeviceInfo(extractDeviceInfo(request));
        session.setIpAddress(extractIpAddress(request));
        session.setActive(true);

        sessionRepository.save(session);
        log.info("Created new session for user: {} with JTI: {}", user.getEmail(), jti);

        return session;
    }

    /**
     * Validate if the JTI from access token is active
     */
    public boolean isSessionActive(String jti) {
        return sessionRepository.findByJtiAndIsActiveTrue(jti).isPresent();
    }

    /**
     * Refresh the session with new tokens
     */
    @Transactional
    public UserSession refreshSession(String oldRefreshToken, User user, HttpServletRequest request) {
        // Validate old refresh token
        UserSession oldSession = sessionRepository.findByRefreshTokenAndIsActiveTrue(oldRefreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired refresh token"));

        // Verify the refresh token belongs to the user
        if (!oldSession.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Refresh token does not belong to the user");
        }

        // Deactivate old session
        oldSession.setActive(false);
        sessionRepository.save(oldSession);

        // Create new session with new JTI and tokens
        return createSession(user, request);
    }

    /**
     * Logout - deactivate session by JTI
     */
    @Transactional
    public void logoutSession(String jti) {
        sessionRepository.deactivateSessionByJti(jti);
        log.info("Deactivated session with JTI: {}", jti);
    }

    /**
     * Get all active sessions for a user (useful for admin/user dashboard)
     */
    public List<UserSession> getActiveSessions(User user) {
        return sessionRepository.findByUserAndIsActiveTrue(user);
    }

    /**
     * Update last accessed time for session
     */
    @Transactional
    public void updateLastAccessed(String jti) {
        sessionRepository.findByJtiAndIsActiveTrue(jti).ifPresent(session -> {
            session.setLastAccessedAt(LocalDateTime.now());
            sessionRepository.save(session);
        });
    }

    private String extractDeviceInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? userAgent : "Unknown Device";
    }

    private String extractIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}