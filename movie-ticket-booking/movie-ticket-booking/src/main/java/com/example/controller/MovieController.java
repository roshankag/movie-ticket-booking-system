package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.dto.MovieDTO;
import com.example.mapper.MovieMapper;
import com.example.service.MovieService;

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

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieController {

    @Inject
    MovieService movieService;

    private final MovieMapper movieMapper = MovieMapper.INSTANCE;

    @GET
    public Response getAllMovies() {
        try {
            List<MovieDTO> movies = movieService.listAllMovies();
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
            MovieDTO movie = movieService.findMovieById(id);
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
    public Response createMovie(MovieDTO movieDTO) {
        try {
            MovieDTO createdMovie = movieService.createMovie(movieDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Movie created successfully with ID: " + createdMovie.getId());
            response.put("data", createdMovie);
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
    public Response updateMovie(@PathParam("id") Long id, MovieDTO movieDTO) {
        try {
            movieDTO.setId(id);
            MovieDTO updatedMovie = movieService.updateMovie(movieDTO);
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
            MovieDTO movie = movieService.findMovieById(id);
            if (movie != null) {
                movieService.deleteMovie(id);
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Movie with ID " + id + " deleted successfully.");
                return Response.ok(response).build();
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Movie with ID " + id + " not found.");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error occurred while deleting movie: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}
