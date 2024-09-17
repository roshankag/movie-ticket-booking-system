package com.example.controller;

import com.example.entity.Showtimes;
import com.example.service.ShowtimeService;

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

@Path("/showtimes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShowtimeController {

    private static final Logger LOGGER = Logger.getLogger(ShowtimeController.class.getName());

    @Inject
    ShowtimeService showtimeService;

    @GET
    public Response getAllShowtimes() {
        try {
            List<Showtimes> showtimes = showtimeService.listAllShowtimes();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Showtimes retrieved successfully.");
            response.put("data", showtimes);
            return Response.ok(response).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving showtimes", e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error retrieving showtimes: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getShowtimeById(@PathParam("id") Long id) {
        try {
            Showtimes showtime = showtimeService.findShowtimeById(id);
            Map<String, Object> response = new HashMap<>();
            if (showtime != null) {
                response.put("message", "Showtime with ID " + id + " retrieved successfully.");
                response.put("data", showtime);
                return Response.ok(response).build();
            } else {
                response.put("message", "Showtime with ID " + id + " not found.");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving showtime with ID: " + id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error retrieving showtime: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @POST
    @Transactional
    public Response createShowtime(Showtimes showtime) {
        try {
            showtimeService.createShowtime(showtime);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Showtime created successfully with ID: " + showtime.getId());
            response.put("data", showtime);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating showtime", e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error creating showtime: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response updateShowtime(@PathParam("id") Long id, Showtimes showtime) {
        try {
            showtime.setId(id);
            Showtimes updatedShowtime = showtimeService.updateShowtime(showtime);
            Map<String, Object> response = new HashMap<>();
            if (updatedShowtime != null) {
                response.put("message", "Showtime with ID " + id + " updated successfully.");
                response.put("data", updatedShowtime);
                return Response.ok(response).build();
            } else {
                response.put("message", "Showtime with ID " + id + " not found.");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating showtime with ID: " + id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error updating showtime: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteShowtime(@PathParam("id") Long id) {
        try {
            Showtimes showtime = showtimeService.findShowtimeById(id);
            Map<String, Object> response = new HashMap<>();
            if (showtime != null) {
                showtimeService.deleteShowtime(id);
                response.put("message", "Showtime with ID " + id + " deleted successfully.");
                return Response.ok(response).build();
            } else {
                response.put("message", "Showtime with ID " + id + " not found.");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting showtime with ID: " + id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error deleting showtime: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}
