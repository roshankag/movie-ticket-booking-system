package com.example.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.dto.PaymentDTO;
import com.example.service.PaymentService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {

    @Inject
    private PaymentService paymentService;

    /**
     * Retrieves a list of all payments.
     * @return Response containing the list of all payments.
     */
    @GET
    public Response getAllPayments() {
        try {
            List<PaymentDTO> payments = paymentService.listAllPayments();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Payments retrieved successfully.");
            response.put("data", payments);
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while retrieving payments: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    /**
     * Retrieves a payment by its ID.
     * @param id The ID of the payment to retrieve.
     * @return Response containing the payment details if found.
     */
    @GET
    @Path("/{id}")
    public Response getPaymentById(@PathParam("id") Long id) {
        try {
            PaymentDTO payment = paymentService.findPaymentById(id);
            Map<String, Object> response = new HashMap<>();
            if (payment != null) {
                response.put("message", "Payment with ID " + id + " retrieved successfully.");
                response.put("data", payment);
                return Response.ok(response).build();
            } else {
                response.put("message", "Payment with ID " + id + " not found.");
                response.put("data", null);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while retrieving payment: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    /**
     * Creates a new payment.
     * @param paymentDTO The payment details to create.
     * @return Response containing the created payment details.
     */
    @POST
    @Transactional
    public Response createPayment(PaymentDTO paymentDTO) {
        try {
            PaymentDTO createdPayment = paymentService.createPayment(paymentDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Payment created successfully with ID: " + createdPayment.getId());
            response.put("data", createdPayment);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while creating payment: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    /**
     * Updates an existing payment.
     * @param id The ID of the payment to update.
     * @param paymentDTO The updated payment details.
     * @return Response containing the updated payment details if successful.
     */
    @PUT
    @Transactional
    @Path("/{id}")
    public Response updatePayment(@PathParam("id") Long id, PaymentDTO paymentDTO) {
        try {
            paymentDTO.setId(id);
            PaymentDTO updatedPayment = paymentService.updatePayment(paymentDTO);
            Map<String, Object> response = new HashMap<>();
            if (updatedPayment != null) {
                response.put("message", "Payment with ID " + id + " updated successfully.");
                response.put("data", updatedPayment);
                return Response.ok(response).build();
            } else {
                response.put("message", "Payment with ID " + id + " not found.");
                response.put("data", null);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while updating payment: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    /**
     * Deletes a payment by its ID.
     * @param id The ID of the payment to delete.
     * @return Response indicating whether the deletion was successful or not.
     */
    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deletePayment(@PathParam("id") Long id) {
        try {
            PaymentDTO payment = paymentService.findPaymentById(id);
            if (payment != null) {
                paymentService.deletePayment(id);
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Payment with ID " + id + " deleted successfully.");
                return Response.ok(response).build();
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Payment with ID " + id + " not found.");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while deleting payment: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    /**
     * Retrieves payments associated with a specific booking ID.
     * @param bookingId The ID of the booking.
     * @return Response containing the list of payments associated with the booking ID.
     */
    @GET
    @Path("/booking/{bookingId}")
    public Response getPaymentsByBookingId(@PathParam("bookingId") Long bookingId) {
        try {
            List<PaymentDTO> payments = paymentService.findPaymentsByBookingId(bookingId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Payments for booking ID " + bookingId + " retrieved successfully.");
            response.put("data", payments);
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while retrieving payments for booking ID " + bookingId + ": " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    /**
     * Calculates the total payments for a specific booking ID.
     * @param bookingId The ID of the booking.
     * @return Response containing the total payments amount.
     */
    @GET
    @Path("/booking/{bookingId}/total")
    public Response calculateTotalPaymentsForBooking(@PathParam("bookingId") Long bookingId) {
        try {
            Double total = paymentService.calculateTotalPaymentsForBooking(bookingId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Total payments for booking ID " + bookingId + " calculated successfully.");
            response.put("data", total);
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while calculating total payments for booking ID " + bookingId + ": " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    /**
     * Updates the statuses of multiple payments.
     * @param newStatus The new status to set for the payments.
     * @param ids The list of payment IDs to update.
     * @return Response indicating the result of the batch update operation.
     */
    @PUT
    @Path("/batch/status")
    @Transactional
    public Response batchUpdatePaymentStatuses(@QueryParam("ids") List<Long> ids, @QueryParam("status") String newStatus) {
        try {
            paymentService.batchUpdatePaymentStatuses(ids, newStatus);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Payment statuses updated successfully.");
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while updating payment statuses: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    /**
     * Retrieves payments that are overdue based on the given due date.
     * @param dueDate The due date to compare against.
     * @return Response containing the list of overdue payments.
     */
    @GET
    @Path("/overdue")
    public Response getOverduePayments(@QueryParam("dueDate") String dueDateStr) {
        try {
            LocalDateTime dueDate = LocalDateTime.parse(dueDateStr);
            List<PaymentDTO> overduePayments = paymentService.findOverduePayments(dueDate);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Overdue payments retrieved successfully.");
            response.put("data", overduePayments);
            return Response.ok(response).build();
        } catch (DateTimeParseException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Invalid date format. Please use ISO-8601 format.");
            response.put("data", null);
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while retrieving overdue payments: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    /**
     * Retrieves payments filtered by status and date range.
     * @param status The status of payments to filter by.
     * @param startDate The start date of the date range.
     * @param endDate The end date of the date range.
     * @return Response containing the list of payments matching the criteria.
     */
    @GET
    @Path("/filter")
    public Response getPaymentsByStatusAndDateRange(
            @QueryParam("status") String status,
            @QueryParam("startDate") String startDateStr,
            @QueryParam("endDate") String endDateStr) {
        try {
            LocalDateTime startDate = LocalDateTime.parse(startDateStr);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr);
            List<PaymentDTO> payments = paymentService.findPaymentsByStatusAndDateRange(status, startDate, endDate);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Payments filtered by status and date range retrieved successfully.");
            response.put("data", payments);
            return Response.ok(response).build();
        } catch (DateTimeParseException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Invalid date format. Please use ISO-8601 format.");
            response.put("data", null);
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while retrieving payments by status and date range: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}
