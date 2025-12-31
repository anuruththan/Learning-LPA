package com.example.learning_jpa.controller;

import com.example.learning_jpa.dto.GeneralResponseDto;
import com.example.learning_jpa.dto.request.GenreDto;
import com.example.learning_jpa.dto.request.ReservationDto;
import com.example.learning_jpa.service.VendorReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vendor/")
public class VendorReservationController {

    @Autowired
    private VendorReservationService vendorReservationService;

    GeneralResponseDto response;

    @PostMapping("/add/genre")
    public GeneralResponseDto addGenre(@RequestBody GenreDto genreDto) {
        response = new GeneralResponseDto();
        try{
            vendorReservationService.saveGenresForStall(genreDto);
            response.setRes(true);
            response.setMsg("Genre added successfully.");
            response.setStatusCode(200);
            response.setData("Genre added successfully.");
        }
        catch (Exception e){
            response.setRes(false);
            response.setMsg(e.getMessage());
            response.setStatusCode(400);
        }

        return response;
    }

    @PostMapping("/reserve")
    public GeneralResponseDto reserveStalls(@RequestBody ReservationDto reservationDto){
        response = new GeneralResponseDto();
        try{
            vendorReservationService.reserve(reservationDto);
            response.setRes(true);
            response.setMsg("Stalls reserved successfully.");
            response.setStatusCode(200);
            response.setData("Stalls reserved successfully.");
        }
        catch (Exception e){
            response.setRes(false);
            response.setMsg(e.getMessage());
            response.setStatusCode(400);
        }

        return response;
    }

}
