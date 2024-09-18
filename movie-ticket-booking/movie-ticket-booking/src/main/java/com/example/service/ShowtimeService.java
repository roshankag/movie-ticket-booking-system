package com.example.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.dto.ShowtimesDTO;
import com.example.entity.Showtimes;
import com.example.mapper.ShowtimesMapper;
import com.example.repository.ShowtimeRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ShowtimeService {

    @Inject
    ShowtimeRepository showtimesRepository;

    private final ShowtimesMapper showtimesMapper = ShowtimesMapper.INSTANCE;

    public List<ShowtimesDTO> listAllShowtimes() {
        return showtimesRepository.listAll().stream()
                .map(showtimesMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ShowtimesDTO findShowtimeById(Long id) {
        Showtimes showtimes = showtimesRepository.findById(id);
        return showtimesMapper.toDTO(showtimes);
    }

    @Transactional
    public ShowtimesDTO createShowtime(ShowtimesDTO showtimesDTO) {
        Showtimes showtimes = showtimesMapper.toEntity(showtimesDTO);
        showtimesRepository.persist(showtimes);
        return showtimesMapper.toDTO(showtimes);
    }

    @Transactional
    public ShowtimesDTO updateShowtime(ShowtimesDTO showtimesDTO) {
        Showtimes showtimes = showtimesMapper.toEntity(showtimesDTO);
        Showtimes updatedShowtime = showtimesRepository.getEntityManager().merge(showtimes);
        return showtimesMapper.toDTO(updatedShowtime);
    }

    @Transactional
    public void deleteShowtime(Long id) {
        Showtimes showtimes = showtimesRepository.findById(id);
        if (showtimes != null) {
            showtimesRepository.delete(showtimes);
        }
    }
}
