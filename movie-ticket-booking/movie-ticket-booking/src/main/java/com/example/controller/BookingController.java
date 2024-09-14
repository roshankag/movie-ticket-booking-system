package com.example.controller;

import com.example.entity.Bookings;
import com.example.service.BookingService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingController {

    @Inject
    BookingService bookingService;

    @GET
    public List<Bookings> getAllBookings() {
        return bookingService.listAllBookings();
    }

    @GET
    @Path("/{id}")
    public Response getBookingById(@PathParam("id") Long id) {
        Bookings booking = bookingService.findBookingById(id);
        return booking != null ? Response.ok(booking).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response createBooking(Bookings booking) {
        bookingService.createBooking(booking);
        return Response.status(Response.Status.CREATED).entity(booking).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBooking(@PathParam("id") Long id, Bookings booking) {
        booking.setId(id);
        Bookings updatedBooking = bookingService.updateBooking(booking);
        return updatedBooking != null ? Response.ok(updatedBooking).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBooking(@PathParam("id") Long id) {
        bookingService.deleteBooking(id);
        return Response.noContent().build();
    }
}
