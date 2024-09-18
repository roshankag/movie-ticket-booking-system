package com.example.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MovieDTO {
	private Long id;
    private String title;
    private String description;
    private LocalDate releaseDate;
    private Integer durationMinutes;
  
	
}
