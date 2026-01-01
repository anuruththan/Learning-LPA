package com.example.learning_jpa.service;

import com.example.learning_jpa.dto.response.StallDto;
import com.example.learning_jpa.entity.Stall;

import java.util.List;

public interface StallAvailabilityService {
    public List<StallDto> getAvailableStalls();
    public List<StallDto> getReservedStalls();
    public List<StallDto> getAllStalls();
}
