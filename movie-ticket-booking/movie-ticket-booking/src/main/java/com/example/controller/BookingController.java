package com.example.controller;

import com.example.entity.Bookings;
import com.example.service.BookingService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingController {

    @Inject
    BookingService bookingService;

    @GET
    public Response getAllBookings() {
        try {
            List<Bookings> bookings = bookingService.listAllBookings();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Bookings retrieved successfully.");
            response.put("data", bookings);
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while retrieving bookings: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getBookingById(@PathParam("id") Long id) {
        try {
            Bookings booking = bookingService.findBookingById(id);
            Map<String, Object> response = new HashMap<>();
            if (booking != null) {
                response.put("message", "Booking with ID " + id + " retrieved successfully.");
                response.put("data", booking);
                return Response.ok(response).build();
            } else {
                response.put("message", "Booking with ID " + id + " not found.");
                response.put("data", null);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while retrieving booking: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @POST
    @Transactional
    public Response createBooking(Bookings booking) {
        try {
            // Remove id to let the database handle auto-generation
            booking.setId(null);

            // Create the booking
            bookingService.createBooking(booking);

            // Create the response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Booking created successfully.");
            response.put("data", booking);

            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            // Handle the exception
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while creating booking: " + e.getMessage());
            response.put("data", null);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }


    @PUT
    @Transactional
    @Path("/{id}")
    public Response updateBooking(@PathParam("id") Long id, Bookings booking) {
        try {
            booking.setId(id);
            Bookings updatedBooking = bookingService.updateBooking(booking);
            Map<String, Object> response = new HashMap<>();
            if (updatedBooking != null) {
                response.put("message", "Booking with ID " + id + " updated successfully.");
                response.put("data", updatedBooking);
                return Response.ok(response).build();
            } else {
                response.put("message", "Booking with ID " + id + " not found.");
                response.put("data", null);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while updating booking: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteBooking(@PathParam("id") Long id) {
        try {
            bookingService.deleteBooking(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Booking with ID " + id + " deleted successfully.");
            response.put("data", null);  // Optional, can be removed if not needed
            return Response.ok(response).build();  // Use Response.ok() to return a body
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while deleting booking: " + e.getMessage());
            response.put("data", null);  // Optional, can be removed if not needed
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

}
