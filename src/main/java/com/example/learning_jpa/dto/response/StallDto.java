package com.example.learning_jpa.dto.response;

import com.example.learning_jpa.enums.StallSize;
import com.example.learning_jpa.enums.StallStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class StallDto {
    private Long id;
    private StallSize size;
    private String stallCode;
    private StallStatus status;
}
