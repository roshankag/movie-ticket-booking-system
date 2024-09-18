package com.example.service;

import java.util.List;

import com.example.entity.Payments;
import com.example.repository.PaymentRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PaymentService {

    @Inject
    PaymentRepository paymentRepository;

    public List<Payments> listAllPayments() {
        return paymentRepository.listAll();
    }

    public Payments findPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payments createPayment(Payments payment) {
        paymentRepository.persist(payment);
        return payment;
    }

    public Payments updatePayment(Payments payment) {
        return paymentRepository.getEntityManager().merge(payment);
    }

    public boolean deletePayment(Long id) {
        Payments payment = paymentRepository.findById(id);
        if (payment != null) {
            paymentRepository.delete(payment);
            return true; // Deletion successful
        }
        return false; // Payment not found
    }
}
