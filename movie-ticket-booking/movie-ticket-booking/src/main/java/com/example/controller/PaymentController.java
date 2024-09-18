package com.example.controller;

import com.example.dto.PaymentDTO;
import com.example.mapper.PaymentMapper;
import com.example.service.PaymentService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {

    @Inject
    private PaymentService paymentService;

    private final PaymentMapper paymentMapper = PaymentMapper.INSTANCE;

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
}
