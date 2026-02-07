package com.example.learning_jpa.service;

import com.example.learning_jpa.entity.User;
import com.example.learning_jpa.entity.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserSessionService {

    public boolean hasActiveSession(User user);

    /**
     * Create a new session for user
     */
    public UserSession createSession(User user, HttpServletRequest request);

    /**
     * Validate if the JTI from access token is active
     */
    public boolean isSessionActive(String jti);

    /**
     * Refresh the session with new tokens
     */
    public UserSession refreshSession(String oldRefreshToken, User user, HttpServletRequest request);

    /**
     * Logout - deactivate session by JTI
     */
    public void logoutSession(String jti);

    /**
     * Get all active sessions for a user (useful for admin/user dashboard)
     */
    public List<UserSession> getActiveSessions(User user);

    /**
     * Update last accessed time for session
     */
    public void updateLastAccessed(String jti);

}
