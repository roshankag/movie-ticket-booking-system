package com.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;

import com.example.entity.Users;

@ApplicationScoped
public class UserRepository implements PanacheRepository<Users> {
    // Add custom methods if needed
}
