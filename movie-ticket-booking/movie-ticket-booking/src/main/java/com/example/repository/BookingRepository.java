package com.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import com.example.entity.Bookings;

@ApplicationScoped
public class BookingRepository implements PanacheRepositoryBase<Bookings, Long> {
    // Add custom methods if needed
}
