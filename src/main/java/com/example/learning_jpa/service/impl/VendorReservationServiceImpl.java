package com.example.learning_jpa.service.impl;

import com.example.learning_jpa.entity.Genre;
import com.example.learning_jpa.entity.Reservation;
import com.example.learning_jpa.entity.Stall;
import com.example.learning_jpa.entity.Vendor;
import com.example.learning_jpa.enums.StallStatus;
import com.example.learning_jpa.repository.*;
import com.example.learning_jpa.service.EmailService;
import com.example.learning_jpa.service.VendorReservationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendorReservationServiceImpl implements VendorReservationService {

    @Autowired
    private VendorDetailsRepository vendorDetailsRepository;

    @Autowired
    private StallDetailsRepository stallDetailsRepository;

    @Autowired
    private ReservationDetailsRepository reservationDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenreDetailsRepository genreDetailsRepository;

    @Autowired
    private EmailService emailService;


    @Override
    @Transactional
    public void saveGenresForStall(Long stallId, List<String> genreNames) {
        Stall stall = stallDetailsRepository.findById(stallId)
                .orElseThrow(() -> new RuntimeException("Stall not found"));

        // Remove existing genres (update)
        genreDetailsRepository.deleteAll(stall.getGenres());
        stall.getGenres().clear();

        for (String name : genreNames) {
            Genre genre = new Genre();
            genre.setName(name);
            genre.setStall(stall);
            stall.getGenres().add(genre);
        }

        stallDetailsRepository.save(stall);
    }


    @Override
    @Transactional
    public void reserve(Long userId, List<Long> stallIds) {

        Vendor vendor = vendorDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        if (reservationDetailsRepository.countByVendorId(vendor.getId()) + stallIds.size() > 3) {
            throw new RuntimeException("Max 3 stalls allowed");
        }

        for (Long stallId : stallIds) {

            Stall stall = stallDetailsRepository.findById(stallId)
                    .orElseThrow();

            if (stall.getStatus() == StallStatus.RESERVED) {
                throw new RuntimeException("Stall already reserved");
            }

            Reservation reservation = new Reservation();
            reservation.setVendor(vendor);
            reservation.setStall(stall);
            reservation.setReservedAt(LocalDateTime.now());


            String UUID = java.util.UUID.randomUUID().toString();
            reservation.setQrCode(UUID);

            stall.setStatus(StallStatus.RESERVED);

            reservationDetailsRepository.save(reservation);
            stallDetailsRepository.save(stall);

            String userEmail = userRepository.findById(userId).get().getEmail();

            emailService.sendConfirmation(userEmail , UUID);
        }

    }


}
