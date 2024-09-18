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
            // Call the service method to get the list of BookingDTOs
            List<BookingDTO> bookings = bookingService.listAllBookings();

            // Create a response map
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Bookings retrieved successfully.");
            response.put("data", bookings);

            // Return success response with data
            return Response.ok(response).build();
        } catch (Exception e) {
            // Handle any errors and return an appropriate response
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
                // Success response
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Booking retrieved successfully.");
                response.put("data", booking);
                return Response.ok(response).build();
            } else {
                // Booking not found response
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Booking not found.");
                response.put("data", null);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            // Error response
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

            // Success response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Booking created successfully.");
            response.put("data", createdBooking);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            // Error response
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
            bookingDTO.setId(id);  // Set the ID in the DTO
            BookingDTO updatedBooking = bookingService.updateBooking(bookingDTO);

            if (updatedBooking != null) {
                // Success response
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Booking updated successfully.");
                response.put("data", updatedBooking);
                return Response.ok(response).build();
            } else {
                // Booking not found response
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Booking not found.");
                response.put("data", null);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            // Error response
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
            // Attempt to find and delete the booking
            BookingDTO booking = bookingService.findBookingById(id);
            if (booking != null) {
                bookingService.deleteBooking(id);

                // Success response with message
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Booking deleted successfully.");
                return Response.ok(response).build();
            } else {
                // Booking not found response
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Booking not found.");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            // Error response
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error occurred while deleting booking.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
    }

}
