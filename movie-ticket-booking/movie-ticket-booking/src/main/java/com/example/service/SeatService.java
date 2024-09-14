package com.example.service;

import com.example.entity.Seats;
import com.example.repository.SeatRepository;

import jakarta.inject.Inject;
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

    public Seats createSeat(Seats seat) {
        seatRepository.persist(seat);
        return seat;
    }

    public Seats updateSeat(Seats seat) {
        return seatRepository.getEntityManager().merge(seat);
    }

    public void deleteSeat(Long id) {
        Seats seat = seatRepository.findById(id);
        if (seat != null) {
            seatRepository.delete(seat);
        }
    }
}
