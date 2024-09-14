package com.example.controller;

import com.example.entity.Showtimes;
import com.example.service.ShowtimeService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/showtimes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShowtimeController {

    @Inject
    ShowtimeService showtimeService;

    @GET
    public List<Showtimes> getAllShowtimes() {
        return showtimeService.listAllShowtimes();
    }

    @GET
    @Path("/{id}")
    public Response getShowtimeById(@PathParam("id") Long id) {
        Showtimes showtime = showtimeService.findShowtimeById(id);
        return showtime != null ? Response.ok(showtime).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response createShowtime(Showtimes showtime) {
        showtimeService.createShowtime(showtime);
        return Response.status(Response.Status.CREATED).entity(showtime).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateShowtime(@PathParam("id") Long id, Showtimes showtime) {
        showtime.setId(id);
        Showtimes updatedShowtime = showtimeService.updateShowtime(showtime);
        return updatedShowtime != null ? Response.ok(updatedShowtime).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteShowtime(@PathParam("id") Long id) {
        showtimeService.deleteShowtime(id);
        return Response.noContent().build();
    }
}
