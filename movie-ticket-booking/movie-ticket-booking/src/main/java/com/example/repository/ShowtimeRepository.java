package com.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;

import com.example.entity.Showtimes;

@ApplicationScoped
public class ShowtimeRepository implements PanacheRepository<Showtimes> {
    // Add custom methods if needed
}
