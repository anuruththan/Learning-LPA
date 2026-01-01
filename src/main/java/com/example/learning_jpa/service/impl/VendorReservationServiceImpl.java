package com.example.learning_jpa.service.impl;

import com.example.learning_jpa.dto.request.GenreDto;
import com.example.learning_jpa.dto.request.ReservationDto;
import com.example.learning_jpa.entity.*;
import com.example.learning_jpa.enums.StallStatus;
import com.example.learning_jpa.repository.*;
import com.example.learning_jpa.service.EmailService;
import com.example.learning_jpa.service.VendorReservationService;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendorReservationServiceImpl implements VendorReservationService {

    @Autowired
    private VendorDetailsRepository vendorDetailsRepository;

    @Autowired
    private StallDetailsRepository stallDetailsRepository;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private ReservationDetailsRepository reservationDetailsRepository;

    @Autowired
    private GenreDetailsRepository genreDetailsRepository;

    @Autowired
    private EmailService emailService;


    /**
     * Persists genres for stall; updates existing entries
     */
    @Override
    public void saveGenresForStall(GenreDto genreDto) {

        Long stallId = genreDto.getStallId();
        List<String> genreNames = genreDto.getGenreNames();

        Stall stall = stallDetailsRepository.findById(stallId)
                .orElseThrow(() -> new RuntimeException("Stall not found"));

        if(stall.getStatus() == StallStatus.AVAILABLE) {
            throw new RuntimeException("Stall is not reserved");
        }

        User user = userAuthRepository.findByEmail(genreDto.getUserEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Vendor vendor = vendorDetailsRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        List<Reservation> reservation = reservationDetailsRepository.findReservationByVendor(vendor);

        if(reservation.isEmpty()) {
            throw new RuntimeException("Vendor has no reservation");
        }

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
    public void reserve(ReservationDto reservationDto) throws MessagingException, IOException, WriterException {

        List<Long> stallIds = reservationDto.getStallIds();

        User user = userAuthRepository.findByEmail(reservationDto.getUserEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Vendor vendor = vendorDetailsRepository.findByUserId(user.getId())
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

            emailService.sendConfirmation(reservationDto.getUserEmail() , UUID);

            reservationDetailsRepository.save(reservation);
            stallDetailsRepository.save(stall);
        }

    }


}
