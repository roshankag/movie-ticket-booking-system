package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data  // Generates getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor  // Generates a no-argument constructor
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long seatId;
    private LocalDateTime bookingTime;
    private String paymentStatus;
}
