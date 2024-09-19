package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.dto.MovieDTO;
import com.example.service.MovieService;

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

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieController {

    @Inject
    MovieService movieService;

    /**
     * Retrieve a paginated and sorted list of movies.
     * 
     * @param page The page number to retrieve (0-based). Defaults to 0.
     * @param size The number of items per page. Defaults to 10.
     * @param sortBy The field to sort by. Defaults to "title".
     * @param ascending Whether to sort in ascending order. Defaults to true.
     * @return A response containing a message and the list of movies.
     */
    @GET
    public Response getAllMovies(@QueryParam("page") @DefaultValue("0") int page,
                                 @QueryParam("size") @DefaultValue("10") int size,
                                 @QueryParam("sortBy") @DefaultValue("title") String sortBy,
                                 @QueryParam("ascending") @DefaultValue("true") boolean ascending) {
        try {
            List<MovieDTO> movies = movieService.listAllMovies(page, size, sortBy, ascending);
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

    /**
     * Retrieve a movie by its ID.
     * 
     * @param id The ID of the movie to retrieve.
     * @return A response containing a message and the movie details if found, otherwise a not found status.
     */
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

    /**
     * Create a new movie.
     * 
     * @param movieDTO The details of the movie to create.
     * @return A response containing a message and the created movie details.
     */
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

    /**
     * Update an existing movie by its ID.
     * 
     * @param id The ID of the movie to update.
     * @param movieDTO The updated movie details.
     * @return A response containing a message and the updated movie details if found, otherwise a not found status.
     */
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

    /**
     * Delete a movie by its ID.
     * 
     * @param id The ID of the movie to delete.
     * @return A response containing a message confirming deletion or indicating that the movie was not found.
     */
    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteMovie(@PathParam("id") Long id) {
        try {
            if (movieService.existsById(id)) {
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
