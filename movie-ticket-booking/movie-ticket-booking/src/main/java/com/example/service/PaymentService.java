package com.example.service;

import com.example.dto.PaymentDTO;
import com.example.entity.Payments;
import com.example.mapper.PaymentMapper;
import com.example.repository.PaymentRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class PaymentService {

    @Inject
    private PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper = PaymentMapper.INSTANCE;

    /**
     * Retrieves a list of all payments.
     * @return A list of PaymentDTO objects representing all payments.
     */
    public List<PaymentDTO> listAllPayments() {
        return paymentRepository.listAll().stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds a payment by its ID.
     * @param id The ID of the payment to find.
     * @return The PaymentDTO object if found, otherwise null.
     */
    public PaymentDTO findPaymentById(Long id) {
        Optional<Payments> payment = paymentRepository.findByIdOptional(id);
        return payment.map(paymentMapper::toDTO).orElse(null);
    }

    /**
     * Creates a new payment from the provided PaymentDTO.
     * @param paymentDTO The PaymentDTO object containing payment details.
     * @return The created PaymentDTO object.
     */
    @Transactional
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Payments payment = paymentMapper.toEntity(paymentDTO);
        paymentRepository.persist(payment);
        return paymentMapper.toDTO(payment);
    }

    /**
     * Updates an existing payment with the details from the provided PaymentDTO.
     * @param paymentDTO The PaymentDTO object containing updated payment details.
     * @return The updated PaymentDTO object if the payment exists, otherwise null.
     */
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

    /**
     * Deletes a payment by its ID.
     * @param id The ID of the payment to delete.
     */
    @Transactional
    public void deletePayment(Long id) {
        if (paymentRepository.findByIdOptional(id).isPresent()) {
            paymentRepository.deleteById(id);
        }
    }

    // Additional Business Logic Methods

    /**
     * Finds payments associated with a specific booking ID.
     * @param bookingId The ID of the booking.
     * @return A list of PaymentDTO objects associated with the booking ID.
     */
    public List<PaymentDTO> findPaymentsByBookingId(Long bookingId) {
        List<Payments> payments = paymentRepository.find("bookingId", bookingId).list();
        return payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Calculates the total amount of payments for a specific booking ID.
     * @param bookingId The ID of the booking.
     * @return The total amount of payments for the booking.
     */
    public Double calculateTotalPaymentsForBooking(Long bookingId) {
        List<Payments> payments = paymentRepository.find("bookingId", bookingId).list();
        return payments.stream()
                .mapToDouble(Payments::getPaymentAmount)
                .sum();
    }

    /**
     * Updates the status of a payment by its ID.
     * @param id The ID of the payment to update.
     * @param newStatus The new status to set for the payment.
     * @return The updated PaymentDTO object if the payment exists, otherwise null.
     */
    @Transactional
    public PaymentDTO updatePaymentStatus(Long id, String newStatus) {
        Optional<Payments> existingPayment = paymentRepository.findByIdOptional(id);
        if (existingPayment.isPresent()) {
            Payments payment = existingPayment.get();
            payment.setPaymentStatus(newStatus);
            paymentRepository.persist(payment);
            return paymentMapper.toDTO(payment);
        }
        return null;
    }

    /**
     * Finds payments made within a specific date range.
     * @param startDate The start of the date range.
     * @param endDate The end of the date range.
     * @return A list of PaymentDTO objects made within the date range.
     */
    public List<PaymentDTO> findPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Payments> payments = paymentRepository.find("paymentTime between ?1 and ?2", startDate, endDate).list();
        return payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a payment with a specific ID exists.
     * @param id The ID of the payment to check.
     * @return True if the payment exists, otherwise false.
     */
    public boolean paymentExists(Long id) {
        return paymentRepository.findByIdOptional(id).isPresent();
    }

    /**
     * Finds payments with a specific status.
     * @param status The status of the payments to find.
     * @return A list of PaymentDTO objects with the specified status.
     */
    public List<PaymentDTO> findPaymentsByStatus(String status) {
        List<Payments> payments = paymentRepository.find("paymentStatus", status).list();
        return payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates the statuses of multiple payments in a batch operation.
     * @param ids The list of payment IDs to update.
     * @param newStatus The new status to set for the payments.
     */
    @Transactional
    public void batchUpdatePaymentStatuses(List<Long> ids, String newStatus) {
        for (Long id : ids) {
            Optional<Payments> existingPayment = paymentRepository.findByIdOptional(id);
            if (existingPayment.isPresent()) {
                Payments payment = existingPayment.get();
                payment.setPaymentStatus(newStatus);
                paymentRepository.persist(payment);
            }
        }
    }

    /**
     * Finds payments that are overdue based on a given due date.
     * @param dueDate The due date to compare against.
     * @return A list of PaymentDTO objects that are overdue.
     */
    public List<PaymentDTO> findOverduePayments(LocalDateTime dueDate) {
        List<Payments> payments = paymentRepository.find("paymentTime < ?1", dueDate).list();
        return payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Finds payments by status and date range.
     * @param status The status of payments to filter by.
     * @param startDate The start date of the date range.
     * @param endDate The end date of the date range.
     * @return List of PaymentDTO matching the criteria.
     */
    @Transactional
    public List<PaymentDTO> findPaymentsByStatusAndDateRange(String status, LocalDateTime startDate, LocalDateTime endDate) {
        // Use a custom query to find payments by status and date range
        List<Payments> payments = paymentRepository.find("paymentStatus = ?1 and paymentTime between ?2 and ?3", status, startDate, endDate).list();
        
        // Convert the list of Payment entities to PaymentDTOs
        return payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
