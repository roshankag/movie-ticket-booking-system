package com.example.controller;

import com.example.entity.Seats;
import com.example.service.SeatService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/seats")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SeatController {

    @Inject
    SeatService seatService;

    @GET
    public List<Seats> getAllSeats() {
        return seatService.listAllSeats();
    }

    @GET
    @Path("/{id}")
    public Response getSeatById(@PathParam("id") Long id) {
        Seats seat = seatService.findSeatById(id);
        return seat != null ? Response.ok(seat).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response createSeat(Seats seat) {
        seatService.createSeat(seat);
        return Response.status(Response.Status.CREATED).entity(seat).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateSeat(@PathParam("id") Long id, Seats seat) {
        seat.setId(id);
        Seats updatedSeat = seatService.updateSeat(seat);
        return updatedSeat != null ? Response.ok(updatedSeat).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSeat(@PathParam("id") Long id) {
        seatService.deleteSeat(id);
        return Response.noContent().build();
    }
}
