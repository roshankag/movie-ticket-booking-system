package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SeatsDTO {

    private Long id;
    private String seatNumber;
    private Long showtimeId;
    private Boolean isBooked;
}
