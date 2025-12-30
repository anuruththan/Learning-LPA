package com.example.learning_jpa.controller;

import com.example.learning_jpa.dto.GeneralResponseDto;
import com.example.learning_jpa.dto.request.GenreDto;
import com.example.learning_jpa.service.VendorReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vendor/")
public class VendorReservationController {

    @Autowired
    private VendorReservationService vendorReservationService;

    GeneralResponseDto response;

    /**
     * Able to add genre or modify genre
     * Able to book stall
     *
     **/

    @PostMapping("/add/genre")
    public GeneralResponseDto addGenre(GenreDto genreDto) {
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
            response.setStatusCode(500);
        }

        return response;
    }

}
