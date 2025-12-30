package com.example.learning_jpa.service;

import java.util.List;

public interface VendorReservationService {

    /**
     * Updates stall genres; persists changes transactionally
     */
    public void saveGenresForStall(Long stallId, List<String> genreNames);

    /**
     * Reserves stalls for vendor; sends confirmation email
     */
    public void reserve(Long userId, List<Long> stallIds);
}
