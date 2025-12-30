package com.example.learning_jpa.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GenreDto {
    String userEmail;
    Long stallId;
    List<String> genreNames;
}
