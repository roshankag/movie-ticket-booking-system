package com.example.controller;

import com.example.dto.SeatsDTO;
import com.example.service.SeatService;
import com.example.mapper.SeatsMapper;

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

@Path("/seats")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SeatController {

    private static final Logger LOGGER = Logger.getLogger(SeatController.class.getName());

    @Inject
    SeatService seatService;

    private final SeatsMapper seatsMapper = SeatsMapper.INSTANCE;

    @GET
    public Response getAllSeats() {
        try {
            List<SeatsDTO> seats = seatService.listAllSeats();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Seats retrieved successfully.");
            response.put("data", seats);
            return Response.ok(response).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving seats", e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while retrieving seats: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getSeatById(@PathParam("id") Long id) {
        try {
            SeatsDTO seat = seatService.findSeatById(id);
            Map<String, Object> response = new HashMap<>();
            if (seat != null) {
                response.put("message", "Seat with ID " + id + " retrieved successfully.");
                response.put("data", seat);
                return Response.ok(response).build();
            } else {
                response.put("message", "Seat with ID " + id + " not found.");
                response.put("data", null);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving seat with ID: " + id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while retrieving seat: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @POST
    @Transactional
    public Response createSeat(SeatsDTO seatDTO) {
        try {
            SeatsDTO createdSeat = seatService.createSeat(seatDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Seat created successfully with ID: " + createdSeat.getId());
            response.put("data", createdSeat);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating seat", e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while creating seat: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response updateSeat(@PathParam("id") Long id, SeatsDTO seatDTO) {
        try {
            seatDTO.setId(id);
            SeatsDTO updatedSeat = seatService.updateSeat(seatDTO);
            Map<String, Object> response = new HashMap<>();
            if (updatedSeat != null) {
                response.put("message", "Seat with ID " + id + " updated successfully.");
                response.put("data", updatedSeat);
                return Response.ok(response).build();
            } else {
                response.put("message", "Seat with ID " + id + " not found.");
                response.put("data", null);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating seat with ID: " + id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while updating seat: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteSeat(@PathParam("id") Long id) {
        try {
            SeatsDTO seat = seatService.findSeatById(id);
            Map<String, Object> response = new HashMap<>();
            if (seat != null) {
                seatService.deleteSeat(id);
                response.put("message", "Seat with ID " + id + " deleted successfully.");
                return Response.ok(response).build();
            } else {
                response.put("message", "Seat with ID " + id + " not found.");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting seat with ID: " + id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while deleting seat: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}
