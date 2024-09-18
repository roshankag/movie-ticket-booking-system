package com.example.service;

import com.example.dto.SeatsDTO;
import com.example.entity.Seats;
import com.example.mapper.SeatsMapper;
import com.example.repository.SeatRepository;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SeatService {

    @Inject
    SeatRepository seatRepository;

    private final SeatsMapper seatsMapper = SeatsMapper.INSTANCE;

    public List<SeatsDTO> listAllSeats() {
        return seatRepository.listAll().stream()
                .map(seatsMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SeatsDTO findSeatById(Long id) {
        Seats seat = seatRepository.findById(id);
        return seatsMapper.toDTO(seat);
    }

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

    @Transactional
    public SeatsDTO updateSeat(SeatsDTO seatDTO) {
        Seats seat = seatsMapper.toEntity(seatDTO);
        return seatsMapper.toDTO(seatRepository.getEntityManager().merge(seat));
    }

    @Transactional
    public void deleteSeat(Long id) {
        Seats seat = seatRepository.findById(id);
        if (seat != null) {
            seatRepository.delete(seat);
        }
    }
}
