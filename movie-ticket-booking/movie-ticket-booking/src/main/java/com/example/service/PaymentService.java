package com.example.service;

import com.example.entity.Payments;
import com.example.repository.PaymentRepository;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

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

    public void deletePayment(Long id) {
        if (paymentRepository.findById(id) != null) {
            paymentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Payment not found with ID: " + id);
        }
    }
}
