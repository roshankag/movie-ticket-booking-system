package com.example.service;

import com.example.dto.MovieDTO;
import com.example.entity.Movies;
import com.example.mapper.MovieMapper;
import com.example.repository.MovieRepository;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MovieService {

    @Inject
    MovieRepository movieRepository;

    private final MovieMapper movieMapper = MovieMapper.INSTANCE;

    public List<MovieDTO> listAllMovies() {
        return movieRepository.listAll().stream()
            .map(movieMapper::toDto)
            .collect(Collectors.toList());
    }

    public MovieDTO findMovieById(Long id) {
        Movies movie = movieRepository.findById(id);
        return movie != null ? movieMapper.toDto(movie) : null;
    }

    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movies movie = movieMapper.toEntity(movieDTO);
        movieRepository.persist(movie);
        return movieMapper.toDto(movie);
    }

    public MovieDTO updateMovie(MovieDTO movieDTO) {
        Movies movie = movieMapper.toEntity(movieDTO);
        Movies updatedMovie = movieRepository.getEntityManager().merge(movie);
        return movieMapper.toDto(updatedMovie);
    }

    public void deleteMovie(Long id) {
        Movies movie = movieRepository.findById(id);
        if (movie != null) {
            movieRepository.delete(movie);
        }
    }
}
