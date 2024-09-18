package com.example.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShowtimesDTO {
    private Long id;
    private Long movieId;
    private LocalDateTime showtime;
    
}