package com.example.controller;

import com.example.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    UserService userService;

    @POST
    @Path("/register")
    public Response register(Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email");
        String password = request.get("password");

        String result = userService.register(username, email, password);
        return Response.ok(result).build();
    }

    @POST
    @Path("/login")
    public Response login(Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        String token = userService.login(email, password);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return Response.ok(response).build();
    }
}
