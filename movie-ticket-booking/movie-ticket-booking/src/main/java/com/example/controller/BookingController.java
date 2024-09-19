package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.dto.BookingDTO;
import com.example.mapper.BookingMapper;
import com.example.service.BookingService;

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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingController {

    @Inject
    BookingService bookingService;

    private final BookingMapper bookingMapper = BookingMapper.INSTANCE;

    @GET
    public Response getAllBookings() {
        try {
            List<BookingDTO> bookings = bookingService.listAllBookings();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Bookings retrieved successfully.");
            response.put("data", bookings);
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred while retrieving bookings.");
            errorResponse.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getBookingById(@PathParam("id") Long id) {
        try {
            BookingDTO booking = bookingService.findBookingById(id);
            if (booking != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Booking retrieved successfully.");
                response.put("data", booking);
                return Response.ok(response).build();
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Booking not found.");
                response.put("data", null);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred while retrieving booking.");
            errorResponse.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
    }

    @POST
    @Transactional
    public Response createBooking(BookingDTO bookingDTO) {
        try {
            BookingDTO createdBooking = bookingService.createBooking(bookingDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Booking created successfully.");
            response.put("data", createdBooking);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred while creating booking.");
            errorResponse.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response updateBooking(@PathParam("id") Long id, BookingDTO bookingDTO) {
        try {
            bookingDTO.setId(id);
            BookingDTO updatedBooking = bookingService.updateBooking(bookingDTO);
            if (updatedBooking != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Booking updated successfully.");
                response.put("data", updatedBooking);
                return Response.ok(response).build();
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Booking not found.");
                response.put("data", null);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred while updating booking.");
            errorResponse.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteBooking(@PathParam("id") Long id) {
        try {
            BookingDTO booking = bookingService.findBookingById(id);
            if (booking != null) {
                bookingService.deleteBooking(id);
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Booking deleted successfully.");
                return Response.ok(response).build();
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Booking not found.");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred while deleting booking.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
    }
}
