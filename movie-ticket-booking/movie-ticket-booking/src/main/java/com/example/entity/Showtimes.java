package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data  // Lombok annotation to generate getters, setters, equals, hashCode, and toString
@NoArgsConstructor  // Lombok annotation to generate a no-argument constructor
public class Showtimes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long movieId;
    private LocalDateTime showtime;
}
