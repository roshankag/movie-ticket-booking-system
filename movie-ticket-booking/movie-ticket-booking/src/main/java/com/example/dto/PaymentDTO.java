package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PaymentDTO {
    private Long id;
    private Long bookingId;
    private Double paymentAmount;
    private LocalDateTime paymentTime;
    private String paymentStatus;
}
