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
        Map<String, Object> response = new HashMap<>();
        try {
            String username = request.get("username");
            String email = request.get("email");
            String password = request.get("password");

            String result = userService.register(username, email, password);
            response.put("message", "Registration successful.");
            response.put("data", result);
            return Response.ok(response).build();
        } catch (Exception e) {
            response.put("message", "Error occurred during registration: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @POST
    @Path("/login")
    public Response login(Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = request.get("email");
            String password = request.get("password");

            String token = userService.login(email, password);
            if (token != null) {
                response.put("message", "Login successful.");
                response.put("data", Map.of("token", token));
                return Response.ok(response).build();
            } else {
                response.put("message", "Invalid email or password.");
                response.put("data", null);
                return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
            }
        } catch (Exception e) {
            response.put("message", "Error occurred during login: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}
