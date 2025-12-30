package com.example.learning_jpa.repository;

import com.example.learning_jpa.entity.Stall;
import com.example.learning_jpa.enums.StallStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StallDetailsRepository extends JpaRepository<Stall, Long> {
    List<Stall> findByStatus(StallStatus status);
    List<Stall> findAll();
}
