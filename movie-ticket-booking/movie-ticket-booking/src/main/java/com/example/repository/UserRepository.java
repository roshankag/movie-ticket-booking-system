package com.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import com.example.entity.Users;

import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<Users, Long> {
    
    // Find by user name
    public Optional<Users> findByUsername(String username) {
        return find("username", username).firstResultOptional();
    }

    // Find by email
    public Optional<Users> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
    
    // Add custom methods if needed
}
