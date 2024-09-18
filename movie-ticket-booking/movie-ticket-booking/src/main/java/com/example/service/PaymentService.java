package com.example.service;

import com.example.dto.PaymentDTO;
import com.example.entity.Payments;
import com.example.mapper.PaymentMapper;
import com.example.repository.PaymentRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class PaymentService {

    @Inject
    private PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper = PaymentMapper.INSTANCE;

    public List<PaymentDTO> listAllPayments() {
        return paymentRepository.listAll().stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PaymentDTO findPaymentById(Long id) {
        Optional<Payments> payment = paymentRepository.findByIdOptional(id);
        return payment.map(paymentMapper::toDTO).orElse(null);
    }

    @Transactional
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Payments payment = paymentMapper.toEntity(paymentDTO);
        paymentRepository.persist(payment);
        return paymentMapper.toDTO(payment);
    }

    @Transactional
    public PaymentDTO updatePayment(PaymentDTO paymentDTO) {
        Optional<Payments> existingPayment = paymentRepository.findByIdOptional(paymentDTO.getId());
        if (existingPayment.isPresent()) {
            Payments payment = paymentMapper.toEntity(paymentDTO);
            paymentRepository.persist(payment);
            return paymentMapper.toDTO(payment);
        }
        return null;
    }

    @Transactional
    public void deletePayment(Long id) {
        if (paymentRepository.findByIdOptional(id).isPresent()) {
            paymentRepository.deleteById(id);
        }
    }
}
