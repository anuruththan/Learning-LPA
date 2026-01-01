package com.example.learning_jpa.repository;

import com.example.learning_jpa.entity.Reservation;
import com.example.learning_jpa.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationDetailsRepository extends JpaRepository<Reservation, Long> {
    long countByVendorId(Long vendorId);
    List<Reservation> findReservationByVendor(Vendor vendor);
}
