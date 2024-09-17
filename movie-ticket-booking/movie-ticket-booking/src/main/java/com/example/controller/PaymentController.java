package com.example.controller;

import com.example.entity.Payments;
import com.example.service.PaymentService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {

    private static final Logger LOGGER = Logger.getLogger(PaymentController.class.getName());

    @Inject
    PaymentService paymentService;

    @GET
    public Response getAllPayments() {
        try {
            List<Payments> payments = paymentService.listAllPayments();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Payments retrieved successfully.");
            response.put("data", payments);
            return Response.ok(response).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving payments", e);
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
            Payments payment = paymentService.findPaymentById(id);
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
            LOGGER.log(Level.SEVERE, "Error retrieving payment with ID: " + id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while retrieving payment: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @POST
    @Transactional
    public Response createPayment(Payments payment) {
        try {
            paymentService.createPayment(payment);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Payment created successfully.");
            response.put("data", payment);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating payment", e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while creating payment: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response updatePayment(@PathParam("id") Long id, Payments payment) {
        try {
            payment.setId(id);
            Payments updatedPayment = paymentService.updatePayment(payment);
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
            LOGGER.log(Level.SEVERE, "Error updating payment with ID: " + id, e);
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
            Payments payment = paymentService.findPaymentById(id);
            Map<String, Object> response = new HashMap<>();
            if (payment != null) {
                paymentService.deletePayment(id);
                response.put("message", "Payment with ID " + id + " deleted successfully.");
                return Response.ok(response).build();
            } else {
                response.put("message", "Payment with ID " + id + " not found.");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting payment with ID: " + id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while deleting payment: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}
