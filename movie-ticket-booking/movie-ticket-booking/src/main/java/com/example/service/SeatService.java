package com.example.service;

import com.example.dto.SeatsDTO;
import com.example.entity.Seats;
import com.example.mapper.SeatsMapper;
import com.example.repository.SeatRepository;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class SeatService {

    @Inject
    SeatRepository seatRepository;

    private final SeatsMapper seatsMapper = SeatsMapper.INSTANCE;

    /**
     * Retrieves a list of all seats.
     * @return List of SeatsDTO representing all seats.
     */
    public List<SeatsDTO> listAllSeats() {
        return seatRepository.listAll().stream()
                .map(seatsMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds a seat by its ID.
     * @param id ID of the seat to be retrieved.
     * @return SeatsDTO representing the seat with the given ID, or null if not found.
     */
    public SeatsDTO findSeatById(Long id) {
        Seats seat = seatRepository.findById(id);
        return seatsMapper.toDTO(seat);
    }

    /**
     * Creates or updates a seat.
     * @param seatDTO DTO representing the seat to be created or updated.
     * @return SeatsDTO representing the created or updated seat.
     */
    @Transactional
    public SeatsDTO createSeat(SeatsDTO seatDTO) {
        Seats seat = seatsMapper.toEntity(seatDTO);
        if (seat.getId() == null) {
            seatRepository.persist(seat);
        } else {
            seat = seatRepository.getEntityManager().merge(seat);
        }
        return seatsMapper.toDTO(seat);
    }

    /**
     * Updates an existing seat.
     * @param seatDTO DTO representing the seat to be updated.
     * @return SeatsDTO representing the updated seat, or null if the seat was not found.
     */
    @Transactional
    public SeatsDTO updateSeat(SeatsDTO seatDTO) {
        Seats seat = seatsMapper.toEntity(seatDTO);
        return seatsMapper.toDTO(seatRepository.getEntityManager().merge(seat));
    }

    /**
     * Deletes a seat by its ID.
     * @param id ID of the seat to be deleted.
     */
    @Transactional
    public void deleteSeat(Long id) {
        Seats seat = seatRepository.findById(id);
        if (seat != null) {
            seatRepository.delete(seat);
        }
    }

    /**
     * Finds seats by their availability (dummy implementation).
     * @param availability Availability criteria for filtering seats.
     * @return List of SeatsDTO representing seats with the given availability.
     */
    public List<SeatsDTO> findSeatsByAvailability(boolean available) {
        return seatRepository.listAll().stream()
                .filter(seat -> isAvailable(seat) == available)
                .map(seatsMapper::toDTO)
                .collect(Collectors.toList());
    }

    private boolean isAvailable(Seats seat) {
        // Placeholder logic for determining if a seat is available
        // Modify this based on actual seat attributes or logic
        return true; // Example implementation; replace with actual logic
    }

    /**
     * Finds seats based on specific criteria (e.g., some attribute).
     * @param criteria Criteria for filtering seats.
     * @return List of SeatsDTO representing seats matching the criteria.
     */
    public List<SeatsDTO> findSeatsByCriteria(String criteria) {
        return seatRepository.listAll().stream()
                .filter(seat -> seatMatchesCriteria(seat, criteria))
                .map(seatsMapper::toDTO)
                .collect(Collectors.toList());
    }

    private boolean seatMatchesCriteria(Seats seat, String criteria) {
        // Implement the logic for matching seats based on the criteria
        // Adjust this method based on the actual fields available in the Seats entity
        // Example placeholder logic:
        return seat.toString().contains(criteria); // Replace with actual criteria matching logic
    }

    /**
     * Books a seat by marking it as booked (dummy implementation).
     * @param seatId ID of the seat to be booked.
     * @return SeatsDTO representing the booked seat, or null if the seat was not found or already booked.
     */
    @Transactional
    public SeatsDTO bookSeat(Long seatId) {
        Seats seat = seatRepository.findById(seatId);
        if (seat != null && isAvailable(seat)) {
            // Assuming there is no status field, modify logic as needed
            // Example placeholder logic for booking a seat
            return seatsMapper.toDTO(seatRepository.getEntityManager().merge(seat));
        }
        return null;
    }

    /**
     * Releases a seat (dummy implementation).
     * @param seatId ID of the seat to be released.
     * @return SeatsDTO representing the released seat, or null if the seat was not found or not booked.
     */
    @Transactional
    public SeatsDTO releaseSeat(Long seatId) {
        Seats seat = seatRepository.findById(seatId);
        if (seat != null) {
            // Assuming there is no status field, modify logic as needed
            // Example placeholder logic for releasing a seat
            return seatsMapper.toDTO(seatRepository.getEntityManager().merge(seat));
        }
        return null;
    }
}
