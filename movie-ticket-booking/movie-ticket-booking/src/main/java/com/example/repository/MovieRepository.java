package com.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;

import com.example.entity.Movies;

@ApplicationScoped
public class MovieRepository implements PanacheRepository<Movies> {
    // Add custom methods if needed
}
