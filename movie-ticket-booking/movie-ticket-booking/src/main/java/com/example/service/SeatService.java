package com.example.service;

import com.example.entity.Seats;
import com.example.repository.SeatRepository;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SeatService {

    @Inject
    SeatRepository seatRepository;

    public List<Seats> listAllSeats() {
        return seatRepository.listAll();
    }

    public Seats findSeatById(Long id) {
        return seatRepository.findById(id);
    }

    @Transactional
    public Seats createSeat(Seats seat) {
        if (seat.getId() == null) {
            // If the seat ID is not set, it should be created.
            seatRepository.persist(seat);
            return seat;
        } else {
            // For update, ensure the entity is managed.
            return updateSeat(seat);
        }
    }

    @Transactional
    public Seats updateSeat(Seats seat) {
        // Use the repository to merge the seat.
        return seatRepository.getEntityManager().merge(seat);
    }

    @Transactional
    public void deleteSeat(Long id) {
        Seats seat = seatRepository.findById(id);
        if (seat != null) {
            seatRepository.delete(seat);
        }
    }
}
