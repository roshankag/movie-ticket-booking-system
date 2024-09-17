package com.example.controller;

import com.example.entity.Movies;
import com.example.service.MovieService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieController {

    @Inject
    MovieService movieService;

    @GET
    public Response getAllMovies() {
        try {
            List<Movies> movies = movieService.listAllMovies();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Movies retrieved successfully.");
            response.put("data", movies);
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while retrieving movies: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getMovieById(@PathParam("id") Long id) {
        try {
            Movies movie = movieService.findMovieById(id);
            Map<String, Object> response = new HashMap<>();
            if (movie != null) {
                response.put("message", "Movie with ID " + id + " retrieved successfully.");
                response.put("data", movie);
                return Response.ok(response).build();
            } else {
                response.put("message", "Movie with ID " + id + " not found.");
                response.put("data", null);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while retrieving movie: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @POST
    @Transactional
    public Response createMovie(Movies movie) {
        try {
            movieService.createMovie(movie);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Movie created successfully with ID: " + movie.getId());
            response.put("data", movie);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while creating movie: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response updateMovie(@PathParam("id") Long id, Movies movie) {
        try {
            movie.setId(id);
            Movies updatedMovie = movieService.updateMovie(movie);
            Map<String, Object> response = new HashMap<>();
            if (updatedMovie != null) {
                response.put("message", "Movie with ID " + id + " updated successfully.");
                response.put("data", updatedMovie);
                return Response.ok(response).build();
            } else {
                response.put("message", "Movie with ID " + id + " not found.");
                response.put("data", null);
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while updating movie: " + e.getMessage());
            response.put("data", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteMovie(@PathParam("id") Long id) {
        try {
            movieService.deleteMovie(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Movie with ID " + id + " deleted successfully.");
            response.put("data", null);  // Optional, can be removed if not needed
            return Response.ok(response).build();  // Use Response.ok() to return a body
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while deleting movie: " + e.getMessage());
            response.put("data", null);  // Optional, can be removed if not needed
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}
