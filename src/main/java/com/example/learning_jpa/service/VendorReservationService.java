package com.example.learning_jpa.service;

import com.example.learning_jpa.dto.request.GenreDto;
import com.example.learning_jpa.dto.request.ReservationDto;

import java.util.List;

public interface VendorReservationService {

    /**
     * Updates stall genres; persists changes transactionally
     * @param genreDto
     */
    public void saveGenresForStall(GenreDto genreDto);

    /**
     * Reserves stalls for vendor; sends confirmation email
     * @param reservationDto
     */
    public void reserve(ReservationDto reservationDto);
}
