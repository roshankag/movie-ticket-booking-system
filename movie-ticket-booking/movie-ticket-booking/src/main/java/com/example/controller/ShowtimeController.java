package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.dto.ShowtimesDTO;
import com.example.mapper.ShowtimesMapper;
import com.example.service.ShowtimeService;

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

@Path("/showtimes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShowtimeController {

    private static final Logger LOGGER = Logger.getLogger(ShowtimeController.class.getName());

    @Inject
    ShowtimeService showtimesService;

    private final ShowtimesMapper showtimesMapper = ShowtimesMapper.INSTANCE;

    @GET
//    @RolesAllowed({"admin", "user"})  // Both 'admin' and 'user' roles can view all showtimes
    public Response getAllShowtimes() {
        try {
            List<ShowtimesDTO> showtimes = showtimesService.listAllShowtimes();
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

    @GET
    @Path("/{id}")
  // @RolesAllowed({"admin", "user"})  // Both roles can view a single showtime by ID
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

    @POST
    @Transactional
  //  @RolesAllowed("admin")  // Only 'admin' role can create new showtimes
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

    @PUT
    @Transactional
    @Path("/{id}")
 //   @RolesAllowed("admin")  // Only 'admin' role can update showtimes
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

    @DELETE
    @Transactional
    @Path("/{id}")
  //  @RolesAllowed("admin")  // Only 'admin' role can delete showtimes
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
