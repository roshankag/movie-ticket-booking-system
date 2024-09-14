package com.example.controller;

import com.example.entity.Users;
import com.example.service.UserService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService;

    @GET
    public List<Users> getAllUsers() {
        return userService.listAllUsers();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        Users user = userService.findUserById(id);
        return user != null ? Response.ok(user).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response createUser(Users user) {
        userService.createUser(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, Users user) {
        user.setId(id);
        Users updatedUser = userService.updateUser(user);
        return updatedUser != null ? Response.ok(updatedUser).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        userService.deleteUser(id);
        return Response.noContent().build();
    }
}
