package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.dto.ShowtimesDTO;
import com.example.service.ShowtimeService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/showtimes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShowtimeController {

    private static final Logger LOGGER = Logger.getLogger(ShowtimeController.class.getName());

    @Inject
    ShowtimeService showtimesService;

    /**
     * Retrieves all showtimes with optional pagination.
     * 
     * @return A JSON response with a message and a list of ShowtimesDTO objects.
     */
    @GET
    public Response getAllShowtimes(
        @QueryParam("page") @DefaultValue("0") int pageNumber,
        @QueryParam("size") @DefaultValue("10") int pageSize) {
        
        try {
            List<ShowtimesDTO> showtimes;
            
            // If pageSize is less than or equal to zero, fetch all showtimes
            if (pageSize <= 0) {
                showtimes = showtimesService.listAllShowtimes(0, Integer.MAX_VALUE);
            } else {
                showtimes = showtimesService.listAllShowtimes(pageNumber, pageSize);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Showtimes retrieved successfully.");
            response.put("data", showtimes);
            return Response.ok(response).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving showtimes", e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while retrieving showtimes: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    /**
     * Retrieves a specific showtime by its ID.
     * 
     * @param id The ID of the showtime to retrieve.
     * @return A JSON response with a message and the ShowtimesDTO object if found, otherwise an error message.
     */
    @GET
    @Path("/{id}")
    public Response getShowtimeById(@PathParam("id") Long id) {
        try {
            ShowtimesDTO showtimes = showtimesService.findShowtimeById(id);
            Map<String, Object> response = new HashMap<>();
            if (showtimes != null) {
                response.put("message", "Showtime with ID " + id + " retrieved successfully.");
                response.put("data", showtimes);
                return Response.ok(response).build();
            } else {
                response.put("message", "Showtime with ID " + id + " not found.");
                response.put("data", null);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving showtime with ID: " + id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while retrieving showtime: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    /**
     * Creates a new showtime.
     * 
     * @param showtimesDTO The DTO containing details of the showtime to be created.
     * @return A JSON response with a message and the created ShowtimesDTO object.
     */
    @POST
    @Transactional
    public Response createShowtime(ShowtimesDTO showtimesDTO) {
        try {
            ShowtimesDTO createdShowtime = showtimesService.createShowtime(showtimesDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Showtime created successfully with ID: " + createdShowtime.getId());
            response.put("data", createdShowtime);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating showtime", e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while creating showtime: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    /**
     * Updates an existing showtime.
     * 
     * @param id The ID of the showtime to update.
     * @param showtimesDTO The DTO containing updated showtime details.
     * @return A JSON response with a message and the updated ShowtimesDTO object if found, otherwise an error message.
     */
    @PUT
    @Transactional
    @Path("/{id}")
    public Response updateShowtime(@PathParam("id") Long id, ShowtimesDTO showtimesDTO) {
        try {
            showtimesDTO.setId(id);
            ShowtimesDTO updatedShowtime = showtimesService.updateShowtime(showtimesDTO);
            Map<String, Object> response = new HashMap<>();
            if (updatedShowtime != null) {
                response.put("message", "Showtime with ID " + id + " updated successfully.");
                response.put("data", updatedShowtime);
                return Response.ok(response).build();
            } else {
                response.put("message", "Showtime with ID " + id + " not found.");
                response.put("data", null);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating showtime with ID: " + id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while updating showtime: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    /**
     * Deletes a specific showtime by its ID.
     * 
     * @param id The ID of the showtime to delete.
     * @return A JSON response with a message confirming deletion or an error message if the showtime was not found.
     */
    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteShowtime(@PathParam("id") Long id) {
        try {
            ShowtimesDTO showtimes = showtimesService.findShowtimeById(id);
            Map<String, Object> response = new HashMap<>();
            if (showtimes != null) {
                showtimesService.deleteShowtime(id);
                response.put("message", "Showtime with ID " + id + " deleted successfully.");
                return Response.ok(response).build();
            } else {
                response.put("message", "Showtime with ID " + id + " not found.");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting showtime with ID: " + id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while deleting showtime: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}
