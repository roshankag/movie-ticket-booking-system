package com.example.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.example.dto.MovieDTO;
import com.example.entity.Movies;
import com.example.mapper.MovieMapper;
import com.example.repository.MovieRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MovieService {

    @Inject
    MovieRepository movieRepository;

    private final MovieMapper movieMapper = MovieMapper.INSTANCE;

    /**
     * Retrieve all movies with pagination and sorting.
     * 
     * @param page The page number to retrieve (0-based).
     * @param size The number of items per page.
     * @param sortBy The field to sort by.
     * @param ascending Whether to sort in ascending order.
     * @return A list of MovieDTOs.
     */
    public List<MovieDTO> listAllMovies(int page, int size, String sortBy, boolean ascending) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid pagination parameters.");
        }

        // Apply pagination and sorting
        String sortOrder = ascending ? "ASC" : "DESC";
        List<Movies> movies = movieRepository.list("ORDER BY " + sortBy + " " + sortOrder, page * size, size);
        return movies.stream()
                     .map(movieMapper::toDto)
                     .collect(Collectors.toList());
    }

    /**
     * Search for movies by title with case-insensitive matching.
     * 
     * @param title The title to search for.
     * @return A list of MovieDTOs matching the search criteria.
     */
    public List<MovieDTO> searchMoviesByTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Search title cannot be null or empty.");
        }

        List<Movies> movies = movieRepository.find("LOWER(title) LIKE LOWER(?1)", "%" + title + "%").list();
        return movies.stream()
                     .map(movieMapper::toDto)
                     .collect(Collectors.toList());
    }

    /**
     * Check if a movie with the given ID exists.
     * 
     * @param id The ID of the movie to check.
     * @return True if the movie exists, false otherwise.
     */
    public boolean existsById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid movie ID.");
        }
        return movieRepository.findByIdOptional(id).isPresent();
    }

    /**
     * Update the duration of a movie identified by ID.
     * 
     * @param id The ID of the movie to update.
     * @param durationMinutes The new duration in minutes.
     * @return The updated MovieDTO.
     */
    public MovieDTO updateMovieDuration(Long id, Integer durationMinutes) {
        if (id == null || id <= 0 || durationMinutes == null || durationMinutes <= 0) {
            throw new IllegalArgumentException("Invalid parameters for updating movie duration.");
        }

        Movies movie = movieRepository.findById(id);
        if (movie == null) {
            throw new IllegalArgumentException("Movie not found for update.");
        }

        movie.setDurationMinutes(durationMinutes);
        movieRepository.getEntityManager().merge(movie);
        return movieMapper.toDto(movie);
    }

    /**
     * Delete multiple movies identified by their IDs.
     * 
     * @param ids A list of movie IDs to delete.
     */
    public void deleteMovies(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("ID list cannot be null or empty.");
        }

        // Delete each movie individually
        ids.forEach(id -> {
            Movies movie = movieRepository.findById(id);
            if (movie != null) {
                movieRepository.delete(movie);
            }
        });
    }

    /**
     * Create a new movie.
     * 
     * @param movieDTO The MovieDTO to create.
     * @return The created MovieDTO.
     */
    public MovieDTO createMovie(MovieDTO movieDTO) {
        validateMovieData(movieDTO);

        Movies movie = movieMapper.toEntity(movieDTO);
        movieRepository.persist(movie);
        return movieMapper.toDto(movie);
    }

    /**
     * Update an existing movie.
     * 
     * @param movieDTO The MovieDTO with updated information.
     * @return The updated MovieDTO.
     */
    public MovieDTO updateMovie(MovieDTO movieDTO) {
        validateMovieData(movieDTO);

        Movies movie = movieMapper.toEntity(movieDTO);
        Movies updatedMovie = movieRepository.getEntityManager().merge(movie);
        return movieMapper.toDto(updatedMovie);
    }

    /**
     * Delete a movie identified by its ID.
     * 
     * @param id The ID of the movie to delete.
     */
    public void deleteMovie(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid movie ID.");
        }

        Movies movie = movieRepository.findById(id);
        if (movie != null) {
            movieRepository.delete(movie);
        } else {
            throw new IllegalArgumentException("Movie not found for deletion.");
        }
    }

    /**
     * Validate movie data before creating or updating.
     * 
     * @param movieDTO The MovieDTO to validate.
     */
    private void validateMovieData(MovieDTO movieDTO) {
        if (movieDTO == null) {
            throw new IllegalArgumentException("Movie data cannot be null.");
        }
        if (movieDTO.getTitle() == null || movieDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (movieDTO.getReleaseDate() == null || movieDTO.getReleaseDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid release date.");
        }
        if (movieDTO.getDurationMinutes() == null || movieDTO.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Duration must be a positive integer.");
        }
    }
    
 // // Method to find a movie by ID
    public MovieDTO findMovieById(Long id) {
        Movies movie = movieRepository.findById(id);  // Adjust based on your repository method
        return movie != null ? movieMapper.toDto(movie) : null;  // Convert entity to DTO
    }

    
}
