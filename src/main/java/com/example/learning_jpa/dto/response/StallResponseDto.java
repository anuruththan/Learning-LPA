package com.example.learning_jpa.dto.response;

import com.example.learning_jpa.entity.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StallResponseDto{
    List<String> genreNames;
    Long stallId;
    Reservation reservation;
}
