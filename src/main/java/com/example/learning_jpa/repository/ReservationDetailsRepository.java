package com.example.learning_jpa.repository;

import com.example.learning_jpa.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationDetailsRepository extends JpaRepository<Reservation, Long> {
    long countByVendorId(Long vendorId);
}
