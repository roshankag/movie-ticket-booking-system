package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.dto.SeatsDTO;
import com.example.service.SeatService;

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

@Path("/seats")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SeatController {

    private static final Logger LOGGER = Logger.getLogger(SeatController.class.getName());

    @Inject
    SeatService seatService;

    /**
     * Retrieves a list of all seats.
     * @return Response with a message and a list of all seats.
     */
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

    /**
     * Retrieves a seat by its ID.
     * @param id ID of the seat to be retrieved.
     * @return Response with a message and the seat details, or an error message if not found.
     */
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

    /**
     * Creates a new seat.
     * @param seatDTO DTO representing the seat to be created.
     * @return Response with a message and the created seat details.
     */
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

    /**
     * Updates an existing seat.
     * @param id ID of the seat to be updated.
     * @param seatDTO DTO representing the updated seat details.
     * @return Response with a message and the updated seat details, or an error message if not found.
     */
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

    /**
     * Deletes a seat by its ID.
     * @param id ID of the seat to be deleted.
     * @return Response with a message indicating success or failure.
     */
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

    /**
     * Books a seat by its ID.
     * @param seatId ID of the seat to be booked.
     * @return Response with a message and the booked seat details, or an error message if the seat cannot be booked.
     */
    @POST
    @Path("/{id}/book")
    @Transactional
    public Response bookSeat(@PathParam("id") Long seatId) {
        try {
            SeatsDTO bookedSeat = seatService.bookSeat(seatId);
            Map<String, Object> response = new HashMap<>();
            if (bookedSeat != null) {
                response.put("message", "Seat with ID " + seatId + " booked successfully.");
                response.put("data", bookedSeat);
                return Response.ok(response).build();
            } else {
                response.put("message", "Seat with ID " + seatId + " could not be booked.");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error booking seat with ID: " + seatId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while booking seat: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    /**
     * Releases a booked seat by its ID.
     * @param seatId ID of the seat to be released.
     * @return Response with a message and the released seat details, or an error message if the seat cannot be released.
     */
    @POST
    @Path("/{id}/release")
    @Transactional
    public Response releaseSeat(@PathParam("id") Long seatId) {
        try {
            SeatsDTO releasedSeat = seatService.releaseSeat(seatId);
            Map<String, Object> response = new HashMap<>();
            if (releasedSeat != null) {
                response.put("message", "Seat with ID " + seatId + " released successfully.");
                response.put("data", releasedSeat);
                return Response.ok(response).build();
            } else {
                response.put("message", "Seat with ID " + seatId + " could not be released.");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error releasing seat with ID: " + seatId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while releasing seat: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}
