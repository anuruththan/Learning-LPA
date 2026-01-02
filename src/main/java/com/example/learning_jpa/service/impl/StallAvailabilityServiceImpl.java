package com.example.learning_jpa.service.impl;

import com.example.learning_jpa.dto.response.StallDto;
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
    public List<StallDto> getAvailableStalls() {
        return stallDetailsRepository.findByStatus(StallStatus.AVAILABLE).stream()
                .map(stall -> {
                    StallDto dto = new StallDto();
                    dto.setId(stall.getId());
                    dto.setSize(stall.getSize());
                    dto.setStallCode(stall.getStallCode());
                    dto.setStatus(stall.getStatus());
                    return dto;
                })
                .toList();
    }

    @Override
    public List<StallDto> getReservedStalls() {
        return stallDetailsRepository.findByStatus(StallStatus.RESERVED).stream()
                .map(stall -> {
                    StallDto dto = new StallDto();
                    dto.setId(stall.getId());
                    dto.setSize(stall.getSize());
                    dto.setStallCode(stall.getStallCode());
                    dto.setStatus(stall.getStatus());
                    return dto;
                })
                .toList();
    }

    @Override
    public List<StallDto> getAllStalls() {
        return stallDetailsRepository.findAll().stream()
                .map(stall -> {
                    StallDto dto = new StallDto();
                    dto.setId(stall.getId());
                    dto.setSize(stall.getSize());
                    dto.setStallCode(stall.getStallCode());
                    dto.setStatus(stall.getStatus());
                    return dto;
                })
                .toList();
    }
}
