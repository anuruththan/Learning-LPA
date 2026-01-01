package com.example.learning_jpa.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReservationDto {
    String userEmail;
    List<Long> stallIds;
}
