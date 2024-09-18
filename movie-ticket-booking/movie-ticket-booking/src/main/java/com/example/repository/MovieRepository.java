package com.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import com.example.entity.Movies;

@ApplicationScoped
public class MovieRepository implements PanacheRepositoryBase<Movies, Long> {
    // Add custom methods if needed
}
