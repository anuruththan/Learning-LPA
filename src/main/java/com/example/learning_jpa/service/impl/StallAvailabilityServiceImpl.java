package com.example.learning_jpa.service.impl;

import com.example.learning_jpa.entity.Stall;
import com.example.learning_jpa.enums.StallStatus;
import com.example.learning_jpa.repository.StallDetailsRepository;
import com.example.learning_jpa.service.StallAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StallAvailabilityServiceImpl implements StallAvailabilityService {

    @Autowired
    private StallDetailsRepository stallDetailsRepository;

    @Override
    public List<Stall> getAvailableStalls(){
        return stallDetailsRepository.findByStatus(StallStatus.AVAILABLE);
    }

    @Override
    public List<Stall> getReservedStalls(){
        return stallDetailsRepository.findByStatus(StallStatus.RESERVED);
    }

    @Override
    public List<Stall> getAllStalls(){
        return stallDetailsRepository.findAll();
    }
}
