package com.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import com.example.entity.Showtimes;

@ApplicationScoped
public class ShowtimeRepository implements PanacheRepositoryBase<Showtimes, Long> {
    // Add custom methods if needed
}
