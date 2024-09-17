package com.example.controller;

import com.example.entity.Users;
import com.example.service.UserService;

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

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName()); // Added logging to record exceptions and errors for better debugging and tracking

    
    @Inject
    UserService userService;

    @GET
    public Response getAllUsers() {
        try {
            List<Users> users = userService.listAllUsers();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Users retrieved successfully.");
            response.put("data", users);
            return Response.ok(response).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving users", e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error retrieving users: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        try {
            Users user = userService.findUserById(id);
            Map<String, Object> response = new HashMap<>();
            if (user != null) {
                response.put("message", "User with ID " + id + " retrieved successfully.");
                response.put("data", user);
                return Response.ok(response).build();
            } else {
                response.put("message", "User with ID " + id + " not found.");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user with ID: " + id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error retrieving user: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @POST
    @Transactional
    public Response createUser(Users user) {
        try {
            userService.createUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User created successfully with ID: " + user.getId());
            response.put("data", user);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating user", e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error creating user: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, Users user) {
        try {
            user.setId(id);
            Users updatedUser = userService.updateUser(user);
            Map<String, Object> response = new HashMap<>();
            if (updatedUser != null) {
                response.put("message", "User with ID " + id + " updated successfully.");
                response.put("data", updatedUser);
                return Response.ok(response).build();
            } else {
                response.put("message", "User with ID " + id + " not found.");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating user with ID: " + id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error updating user: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        try {
            Users user = userService.findUserById(id);
            Map<String, Object> response = new HashMap<>();
            if (user != null) {
                userService.deleteUser(id);
                response.put("message", "User with ID " + id + " deleted successfully.");
                return Response.ok(response).build();
            } else {
                response.put("message", "User with ID " + id + " not found.");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting user with ID: " + id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error deleting user: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}
