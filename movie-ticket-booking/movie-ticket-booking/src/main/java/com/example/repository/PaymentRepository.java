package com.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;

import com.example.entity.Payments;

@ApplicationScoped
public class PaymentRepository implements PanacheRepository<Payments> {
    // Add custom methods if needed
}
