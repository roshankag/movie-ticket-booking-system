package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.dto.UserDTO;
import com.example.mapper.UserMapper;
import com.example.service.UserService;

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

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Inject
    UserService userService;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @GET
//    @RolesAllowed({"admin", "user"})  // Both 'admin' and 'user' roles can view all users
    public Response getAllUsers() {
        try {
            List<UserDTO> users = userService.listAllUsers();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Users retrieved successfully.");
            response.put("data", users);
            return Response.ok(response).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving users", e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while retrieving users: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @GET
    @Path("/{id}")
//    @RolesAllowed({"admin", "user"})  // Both roles can view a single user by ID
    public Response getUserById(@PathParam("id") Long id) {
        try {
            UserDTO user = userService.findUserById(id);
            Map<String, Object> response = new HashMap<>();
            if (user != null) {
                response.put("message", "User with ID " + id + " retrieved successfully.");
                response.put("data", user);
                return Response.ok(response).build();
            } else {
                response.put("message", "User with ID " + id + " not found.");
                response.put("data", null);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user with ID: " + id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while retrieving user: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @POST
    @Transactional
//    @RolesAllowed("admin")  // Only 'admin' role can create new users
    public Response createUser(UserDTO userDTO) {
        try {
            UserDTO createdUser = userService.createUser(userDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User created successfully with ID: " + createdUser.getId());
            response.put("data", createdUser);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating user", e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while creating user: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @PUT
    @Transactional
    @Path("/{id}")
//    @RolesAllowed("admin")  // Only 'admin' role can update users
    public Response updateUser(@PathParam("id") Long id, UserDTO userDTO) {
        try {
            userDTO.setId(id);
            UserDTO updatedUser = userService.updateUser(userDTO);
            Map<String, Object> response = new HashMap<>();
            if (updatedUser != null) {
                response.put("message", "User with ID " + id + " updated successfully.");
                response.put("data", updatedUser);
                return Response.ok(response).build();
            } else {
                response.put("message", "User with ID " + id + " not found.");
                response.put("data", null);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating user with ID: " + id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while updating user: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
//    @RolesAllowed("admin")  // Only 'admin' role can delete users
    public Response deleteUser(@PathParam("id") Long id) {
        try {
            UserDTO user = userService.findUserById(id);
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
            response.put("message", "Error occurred while deleting user: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}
