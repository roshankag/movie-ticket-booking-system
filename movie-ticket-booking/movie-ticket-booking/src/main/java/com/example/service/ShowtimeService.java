package com.example.service;

import com.example.entity.Showtimes;
import com.example.repository.ShowtimeRepository;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ShowtimeService {

    @Inject
    ShowtimeRepository showtimeRepository;

    public List<Showtimes> listAllShowtimes() {
        return showtimeRepository.listAll();
    }

    public Showtimes findShowtimeById(Long id) {
        return showtimeRepository.findById(id);
    }

    public Showtimes createShowtime(Showtimes showtime) {
        showtimeRepository.persist(showtime);
        return showtime;
    }

    public Showtimes updateShowtime(Showtimes showtime) {
        return showtimeRepository.getEntityManager().merge(showtime);
    }

    public void deleteShowtime(Long id) {
        Showtimes showtime = showtimeRepository.findById(id);
        if (showtime != null) {
            showtimeRepository.delete(showtime);
        }
    }
}
