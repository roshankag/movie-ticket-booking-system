package com.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import com.example.entity.Seats;

@ApplicationScoped
public class SeatRepository implements PanacheRepositoryBase<Seats, Long> {
    // Add custom methods if needed
}
