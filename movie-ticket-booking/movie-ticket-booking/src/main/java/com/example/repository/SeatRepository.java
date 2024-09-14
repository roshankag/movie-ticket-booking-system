package com.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;

import com.example.entity.Seats;

@ApplicationScoped
public class SeatRepository implements PanacheRepository<Seats> {
    // Add custom methods if needed
}
