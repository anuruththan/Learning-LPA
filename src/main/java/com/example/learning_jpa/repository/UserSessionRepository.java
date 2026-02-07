package com.example.learning_jpa.repository;

import com.example.learning_jpa.entity.UserSession;
import com.example.learning_jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findByJtiAndIsActiveTrue(String jti);

    Optional<UserSession> findByRefreshTokenAndIsActiveTrue(String refreshToken);

    List<UserSession> findByUserAndIsActiveTrue(User user);

    @Modifying
    @Transactional
    @Query("UPDATE UserSession s SET s.isActive = false WHERE s.user = :user")
    void deactivateAllUserSessions(User user);

    @Modifying
    @Transactional
    @Query("UPDATE UserSession s SET s.isActive = false WHERE s.jti = :jti")
    void deactivateSessionByJti(String jti);

    boolean existsByUserAndIsActiveTrue(User user);
}