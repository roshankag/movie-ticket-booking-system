package com.example.service;

import com.example.entity.Bookings;
import com.example.dto.BookingDTO;
import com.example.mapper.BookingMapper;
import com.example.repository.BookingRepository;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookingService {

    @Inject
    BookingRepository bookingRepository;

    private final BookingMapper bookingMapper = BookingMapper.INSTANCE;

    public List<BookingDTO> listAllBookings() {
        // Assume bookingRepository.listAll() returns List<Bookings>
        List<Bookings> bookings = bookingRepository.listAll();
        return bookings.stream()
                .map(bookingMapper::toBookingDTO) // Convert each Booking entity to DTO
                .collect(Collectors.toList());
    }



    public BookingDTO findBookingById(Long id) {
        Bookings booking = bookingRepository.findById(id);
        return bookingMapper.toBookingDTO(booking);
    }

    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Bookings booking = bookingMapper.toBookingEntity(bookingDTO);
        bookingRepository.persist(booking);
        return bookingMapper.toBookingDTO(booking);
    }

    public BookingDTO updateBooking(BookingDTO bookingDTO) {
        Bookings booking = bookingMapper.toBookingEntity(bookingDTO);
        booking = bookingRepository.getEntityManager().merge(booking);
        return bookingMapper.toBookingDTO(booking);
    }

    public void deleteBooking(Long id) {
        Bookings booking = bookingRepository.findById(id);
        if (booking != null) {
            bookingRepository.delete(booking);
        }
    }
}
