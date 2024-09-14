package com.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;

import com.example.entity.Bookings;

@ApplicationScoped
public class BookingRepository implements PanacheRepository<Bookings> {
    // Add custom methods if needed
}
