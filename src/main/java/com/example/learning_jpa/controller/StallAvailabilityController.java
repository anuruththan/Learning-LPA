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
            response.setMsg("Consist Available Stalls.");
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

    @GetMapping("/reserved")
    public GeneralResponseDto getReservedStalls(){
        GeneralResponseDto response = new GeneralResponseDto();
        try{
            response.setRes(true);
            response.setMsg("Consist Reserved Stalls.");
            response.setStatusCode(200);
            response.setData(stallAvailabilityService.getReservedStalls());
        }
        catch (Exception e){
            response.setRes(false);
        }
        return response;
    }

    @GetMapping("/all")
    public GeneralResponseDto getAllStalls(){
        GeneralResponseDto response = new GeneralResponseDto();
        try{
            response.setRes(true);
            response.setMsg("Consist All Stalls.");
            response.setStatusCode(200);
            response.setData(stallAvailabilityService.getAllStalls());
        }
        catch (Exception e){
            response.setRes(false);
            response.setMsg(e.getMessage());
            response.setStatusCode(500);
        }
        return response;
    }

}
