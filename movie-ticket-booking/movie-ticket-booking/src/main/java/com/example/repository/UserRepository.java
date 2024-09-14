package com.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

import com.example.entity.Users;

@ApplicationScoped
public class UserRepository implements PanacheRepository<Users> {
	
	 // Find by username
    public Optional<Users> findByUsername(String username) {
        return find("username", username).firstResultOptional();
    }

    // Find by email
    public Optional<Users> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
    // Add custom methods if needed
}
