package com.example.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.example.dto.ShowtimesDTO;
import com.example.entity.Showtimes;
import com.example.mapper.ShowtimesMapper;
import com.example.repository.ShowtimeRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ShowtimeService {

    private static final Logger LOGGER = Logger.getLogger(ShowtimeService.class.getName());

    @Inject
    ShowtimeRepository showtimesRepository;

    private final ShowtimesMapper showtimesMapper = ShowtimesMapper.INSTANCE;

    /**
     * Lists all showtimes in the system with optional pagination.
     *
     * @param pageNumber The page number for pagination (0-based index).
     * @param pageSize The number of items per page.
     * @return A list of ShowtimesDTO objects.
     */
    public List<ShowtimesDTO> listAllShowtimes(int pageNumber, int pageSize) {
        try {
            return showtimesRepository.findAll().page(pageNumber, pageSize).stream()
                    .map(showtimesMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error listing all showtimes", e);
            throw new RuntimeException("Unable to retrieve showtimes.", e);
        }
    }

    /**
     * Finds a showtime by its ID.
     *
     * @param id The ID of the showtime to find.
     * @return A ShowtimesDTO object if found, otherwise throws an exception.
     */
    public ShowtimesDTO findShowtimeById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null.");
        }
        try {
            Showtimes showtimes = showtimesRepository.findById(id);
            if (showtimes == null) {
                throw new EntityNotFoundException("Showtime with ID " + id + " does not exist.");
            }
            return showtimesMapper.toDTO(showtimes);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding showtime by ID: " + id, e);
            throw new RuntimeException("Unable to find showtime.", e);
        }
    }

    /**
     * Creates a new showtime.
     *
     * @param showtimesDTO The DTO containing showtime details.
     * @return The created ShowtimesDTO.
     */
    @Transactional
    public ShowtimesDTO createShowtime(ShowtimesDTO showtimesDTO) {
        if (showtimesDTO.getMovieId() == null || showtimesDTO.getShowtime() == null) {
            throw new IllegalArgumentException("Movie ID and Showtime cannot be null.");
        }

        if (showtimesDTO.getShowtime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Showtime must be in the future.");
        }

        try {
            List<Showtimes> existingShowtimes = showtimesRepository.find("movieId = ?1 and showtime = ?2",
                    showtimesDTO.getMovieId(), showtimesDTO.getShowtime()).list();
            if (!existingShowtimes.isEmpty()) {
                throw new IllegalArgumentException("A showtime for this movie already exists at the specified time.");
            }

            Showtimes showtimes = showtimesMapper.toEntity(showtimesDTO);
            showtimesRepository.persist(showtimes);
            return showtimesMapper.toDTO(showtimes);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating showtime", e);
            throw new RuntimeException("Unable to create showtime.", e);
        }
    }

    /**
     * Updates an existing showtime.
     *
     * @param showtimesDTO The DTO containing updated showtime details.
     * @return The updated ShowtimesDTO.
     */
    @Transactional
    public ShowtimesDTO updateShowtime(ShowtimesDTO showtimesDTO) {
        if (showtimesDTO.getId() == null) {
            throw new IllegalArgumentException("Showtime ID cannot be null.");
        }

        try {
            Showtimes existingShowtime = showtimesRepository.findById(showtimesDTO.getId());
            if (existingShowtime == null) {
                throw new EntityNotFoundException("Showtime with ID " + showtimesDTO.getId() + " does not exist.");
            }

            if (showtimesDTO.getShowtime() != null && showtimesDTO.getShowtime().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Showtime must be in the future.");
            }

            existingShowtime.setMovieId(showtimesDTO.getMovieId());
            existingShowtime.setShowtime(showtimesDTO.getShowtime());

            Showtimes updatedShowtime = showtimesRepository.getEntityManager().merge(existingShowtime);
            return showtimesMapper.toDTO(updatedShowtime);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating showtime with ID: " + showtimesDTO.getId(), e);
            throw new RuntimeException("Unable to update showtime.", e);
        }
    }

    /**
     * Deletes a showtime by its ID.
     *
     * @param id The ID of the showtime to delete.
     */
    @Transactional
    public void deleteShowtime(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null.");
        }

        try {
            Showtimes showtimes = showtimesRepository.findById(id);
            if (showtimes == null) {
                throw new EntityNotFoundException("Showtime with ID " + id + " does not exist.");
            }
            showtimesRepository.delete(showtimes);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting showtime with ID: " + id, e);
            throw new RuntimeException("Unable to delete showtime.", e);
        }
    }

    /**
     * Searches showtimes by movie ID with optional date range.
     *
     * @param movieId The ID of the movie to search showtimes for.
     * @param startDate The start date of the search range (inclusive).
     * @param endDate The end date of the search range (inclusive).
     * @return A list of ShowtimesDTO objects matching the search criteria.
     */
    public List<ShowtimesDTO> searchShowtimes(Long movieId, LocalDateTime startDate, LocalDateTime endDate) {
        if (movieId == null) {
            throw new IllegalArgumentException("Movie ID cannot be null.");
        }

        try {
            List<Showtimes> showtimesList;
            if (startDate != null && endDate != null) {
                showtimesList = showtimesRepository.find("movieId = ?1 and showtime between ?2 and ?3",
                        movieId, startDate, endDate).list();
            } else {
                showtimesList = showtimesRepository.find("movieId = ?1", movieId).list();
            }

            return showtimesList.stream()
                    .map(showtimesMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching showtimes", e);
            throw new RuntimeException("Unable to search showtimes.", e);
        }
    }
}
