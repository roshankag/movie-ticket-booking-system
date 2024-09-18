package com.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import com.example.entity.Payments;

@ApplicationScoped
public class PaymentRepository implements PanacheRepositoryBase<Payments, Long> {
    // Add custom methods if needed
}
