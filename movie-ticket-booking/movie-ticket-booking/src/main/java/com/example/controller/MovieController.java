package com.example.controller;

import com.example.entity.Movies;
import com.example.service.MovieService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieController {

    @Inject
    MovieService movieService;

    @GET
    public List<Movies> getAllMovies() {
        return movieService.listAllMovies();
    }

    @GET
    @Path("/{id}")
    public Response getMovieById(@PathParam("id") Long id) {
        Movies movie = movieService.findMovieById(id);
        return movie != null ? Response.ok(movie).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response createMovie(Movies movie) {
        movieService.createMovie(movie);
        return Response.status(Response.Status.CREATED).entity(movie).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateMovie(@PathParam("id") Long id, Movies movie) {
        movie.setId(id);
        Movies updatedMovie = movieService.updateMovie(movie);
        return updatedMovie != null ? Response.ok(updatedMovie).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMovie(@PathParam("id") Long id) {
        movieService.deleteMovie(id);
        return Response.noContent().build();
    }
}
