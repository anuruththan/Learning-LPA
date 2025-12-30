package com.example.learning_jpa.service;

import com.example.learning_jpa.entity.Stall;

import java.util.List;

public interface StallAvailabilityService {
    public List<Stall> getAvailableStalls();
    public List<Stall> getReservedStalls();
    public List<Stall> getAllStalls();
}
