package com.example.service;

import com.example.entity.Movies;
import com.example.repository.MovieRepository;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class MovieService {

    @Inject
    MovieRepository movieRepository;

    public List<Movies> listAllMovies() {
        return movieRepository.listAll();
    }

    public Movies findMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Movies createMovie(Movies movie) {
        movieRepository.persist(movie);
        return movie;
    }

    public Movies updateMovie(Movies movie) {
        return movieRepository.getEntityManager().merge(movie);
    }

    public void deleteMovie(Long id) {
        Movies movie = movieRepository.findById(id);
        if (movie != null) {
            movieRepository.delete(movie);
        }
    }
}
