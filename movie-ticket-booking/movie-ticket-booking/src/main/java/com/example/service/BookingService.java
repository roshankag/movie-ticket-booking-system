package com.example.service;

import com.example.entity.Bookings;
import com.example.repository.BookingRepository;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class BookingService {

    @Inject
    BookingRepository bookingRepository;

    public List<Bookings> listAllBookings() {
        return bookingRepository.listAll();
    }

    public Bookings findBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Bookings createBooking(Bookings booking) {
        bookingRepository.persist(booking);
        return booking;
    }

    public Bookings updateBooking(Bookings booking) {
        return bookingRepository.getEntityManager().merge(booking);
    }

    public void deleteBooking(Long id) {
        Bookings booking = bookingRepository.findById(id);
        if (booking != null) {
            bookingRepository.delete(booking);
        }
    }
}
