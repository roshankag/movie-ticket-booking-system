package com.example.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookingDTO {

	private Long id;
    
	private Long userId;
    private Long seatId;
    private LocalDateTime bookingTime;
    private String paymentStatus;
    
	
}

//DTOs (Data Transfer Objects) and mappers are used to:
//
//Decouple internal data models from external APIs, improving security and flexibility.
//Reduce the amount of data transferred and control the format of the data.
//Maintain clean and maintainable code by separating data transformation logic from business logic.