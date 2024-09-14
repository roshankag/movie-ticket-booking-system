package com.example.controller;

import com.example.entity.Payments;
import com.example.service.PaymentService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {

    @Inject
    PaymentService paymentService;

    @GET
    public List<Payments> getAllPayments() {
        return paymentService.listAllPayments();
    }

    @GET
    @Path("/{id}")
    public Response getPaymentById(@PathParam("id") Long id) {
        Payments payment = paymentService.findPaymentById(id);
        return payment != null ? Response.ok(payment).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response createPayment(Payments payment) {
        paymentService.createPayment(payment);
        return Response.status(Response.Status.CREATED).entity(payment).build();
    }

    @PUT
    @Path("/{id}")
    public Response updatePayment(@PathParam("id") Long id, Payments payment) {
        payment.setId(id);
        Payments updatedPayment = paymentService.updatePayment(payment);
        return updatedPayment != null ? Response.ok(updatedPayment).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePayment(@PathParam("id") Long id) {
        paymentService.deletePayment(id);
        return Response.noContent().build();
    }
}
