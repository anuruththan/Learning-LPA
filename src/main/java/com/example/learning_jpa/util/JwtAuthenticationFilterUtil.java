package com.example.learning_jpa.util;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationFilterUtil extends OncePerRequestFilter {

    @Autowired
    private AccessJwtUtil accessJwtUtil;

    private static final String TOKEN_COOKIE_NAME = "ACCESS_TOKEN";

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> EXCLUDED_PATTERNS = List.of(
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/auth/**"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return EXCLUDED_PATTERNS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = extractTokenFromCookie(request);
            if (jwt != null) {
                String email = accessJwtUtil.extractEmail(jwt);
                String role = accessJwtUtil.extractRole(jwt);
                if (email != null && role != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    Collections.singletonList(
                                            new SimpleGrantedAuthority(role.toString())
                                    )
                            );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("JWT token has been expired {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"res\": false, \"msg\": \"JWT token has expired. Please log in again.\", \"statusCode\": 401}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"res\": false, \"msg\": \"Invalid token.\", \"statusCode\": 403}");
        }
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
