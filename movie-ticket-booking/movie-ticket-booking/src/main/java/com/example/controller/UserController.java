package com.example.controller;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.dto.UserDTO;
import com.example.entity.Users;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Controller class for handling HTTP requests related to user operations.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Inject
    UserService userService;

    /**
     * Registers a new user.
     *
     * @param username the username of the new user
     * @param email the email of the new user
     * @param password the password of the new user
     * @return a response indicating the result of the registration
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(Users request) {
        String result = userService.register(request.getUsername(), request.getEmail(), request.getPassword());
        LOGGER.log(Level.INFO, "User registration result: {0}", result);
        
        if (result.equals("User registered successfully!")) {
            return Response.status(Response.Status.CREATED).entity(result).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
    }

    /**	
     * Authenticates a user and returns a JWT token.
     *
     * @param email the email of the user
     * @param password the password of the user
     * @return a response containing the JWT token or an error message
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Users request) {
        String token = userService.login(request.getEmail(), request.getPassword());
        
        if (token.startsWith("Invalid")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(token).build();
        }
        return Response.ok(Map.of("token", token)).build();
    }

    /**
     * Changes the password for a user.
     *
     * @param id the ID of the user
     * @param newPassword the new password
     * @return a response indicating the result of the password change
     */
    @PUT
    @Path("/changePassword/{id}")
    @Transactional
    public Response changePassword(@PathParam("id") Long id, @QueryParam("newPassword") String newPassword) {
        String result = userService.changePassword(id, newPassword);
        return Response.ok(result).build();
    }

    /**
     * Updates the role of a user.
     *
     * @param id the ID of the user
     * @param newRole the new role for the user
     * @return a response indicating the result of the role update
     */
    @PUT
    @Path("/updateRole/{id}")
    @Transactional
    public Response updateUserRole(@PathParam("id") Long id, @QueryParam("newRole") String newRole) {
        String result = userService.updateUserRole(id, newRole);
        return Response.ok(result).build();
    }

    /**
     * Deactivates a user by deleting their record.
     *
     * @param id the ID of the user
     * @return a response indicating the result of the deactivation
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deactivateUser(@PathParam("id") Long id) {
        String result = userService.deactivateUser(id);
        return Response.ok(result).build();
    }

    /**
     * Lists all users with a specific role.
     *
     * @param role the role to filter users by
     * @return a response containing a list of users with the specified role
     */
    @GET
    @Path("/listByRole")
    public Response listUsersByRole(@QueryParam("role") String role) {
        List<UserDTO> users = userService.listUsersByRole(role);
        return Response.ok(users).build();
    }

    /**
     * Finds users by username.
     *
     * @param username the username to search for
     * @return a response containing a list of users with the specified username
     */
    @GET
    @Path("/findByUsername")
    public Response findUsersByUsername(@QueryParam("username") String username) {
        List<UserDTO> users = userService.findUsersByUsername(username);
        return Response.ok(users).build();
    }

    /**
     * Finds users by email.
     *
     * @param email the email to search for
     * @return a response containing a list of users with the specified email
     */
    @GET
    @Path("/findByEmail")
    public Response findUsersByEmail(@QueryParam("email") String email) {
        List<UserDTO> users = userService.findUsersByEmail(email);
        return Response.ok(users).build();
    }

    /**
     * Lists all users.
     *
     * @return a response containing a list of all users
     */
    @GET
    @Path("/listAll")
    public Response listAllUsers() {
        List<UserDTO> users = userService.listAllUsers();
        return Response.ok(users).build();
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user
     * @return a response containing the user with the specified ID
     */
    @GET
    @Path("/findById/{id}")
    public Response findUserById(@PathParam("id") Long id) {
        UserDTO user = userService.findUserById(id);
        return Response.ok(user).build();
    }

    /**
     * Creates a new user from a UserDTO.
     *
     * @param userDTO the user data transfer object containing user details
     * @return a response containing the created user
     */
    @POST
    @Path("/create")
    @Transactional
    public Response createUser(UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return Response.ok(createdUser).build();
    }

    /**
     * Updates an existing user with the given UserDTO.
     *
     * @param userDTO the user data transfer object containing updated user details
     * @return a response containing the updated user
     */
    @PUT
    @Path("/update")
    @Transactional
    public Response updateUser(UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(userDTO);
        return Response.ok(updatedUser).build();
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user
     * @return a response indicating the result of the deletion
     */
    @DELETE
    @Path("/delete/{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {
        userService.deleteUser(id);
        return Response.noContent().build();
    }
}
