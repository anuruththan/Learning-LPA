package com.example.learning_jpa.controller;

import com.example.learning_jpa.dto.GeneralResponseDto;
import com.example.learning_jpa.service.StallAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stall/")
public class StallAvailabilityController {

    @Autowired
    StallAvailabilityService stallAvailabilityService;

    @GetMapping("/available")
    public GeneralResponseDto getAvailableStalls(){
        GeneralResponseDto response = new GeneralResponseDto();
        try{
            response.setRes(true);
            response.setMsg("Stalls are available.");
            response.setStatusCode(200);
            response.setData(stallAvailabilityService.getAvailableStalls());
        }
        catch (Exception e){
            response.setRes(false);
            response.setMsg(e.getMessage());
            response.setStatusCode(500);
        };
        return response;
    }
}
