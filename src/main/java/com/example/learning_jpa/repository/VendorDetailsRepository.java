package com.example.learning_jpa.repository;

import com.example.learning_jpa.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorDetailsRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByUserId(Long userId);
}
