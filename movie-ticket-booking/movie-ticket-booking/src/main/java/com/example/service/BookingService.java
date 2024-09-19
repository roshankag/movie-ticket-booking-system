package com.example.service;

import com.example.entity.Bookings;
import com.example.dto.BookingDTO;
import com.example.mapper.BookingMapper;
import com.example.repository.BookingRepository;
import com.example.repository.SeatRepository;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookingService {

    @Inject
    BookingRepository bookingRepository;

    @Inject
    SeatRepository seatRepository; // For seat-related validations

    private final BookingMapper bookingMapper = BookingMapper.INSTANCE;

    /**
     * List all bookings.
     * @return List of BookingDTOs representing all bookings.
     */
    public List<BookingDTO> listAllBookings() {
        List<Bookings> bookings = bookingRepository.listAll();
        return bookings.stream()
                .map(bookingMapper::toBookingDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find a booking by its ID.
     * @param id Booking ID
     * @return BookingDTO representing the booking, or null if not found.
     */
    public BookingDTO findBookingById(Long id) {
        Bookings booking = bookingRepository.findById(id);
        return bookingMapper.toBookingDTO(booking);
    }

    /**
     * Create a new booking with validation.
     * @param bookingDTO Booking data transfer object containing booking details.
     * @return BookingDTO representing the created booking.
     * @throws IllegalStateException If validation fails.
     */
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        validateBooking(bookingDTO); // Validation logic

        // Convert DTO to entity
        Bookings booking = bookingMapper.toBookingEntity(bookingDTO);
        
        // Set the booking time and default payment status if not provided
        booking.setBookingTime(LocalDateTime.now());
        if (booking.getPaymentStatus() == null) {
            booking.setPaymentStatus("PENDING");
        }

        // Persist the booking
        bookingRepository.persist(booking);
        return bookingMapper.toBookingDTO(booking);
    }

    /**
     * Update an existing booking with validation.
     * @param bookingDTO Booking data transfer object containing updated details.
     * @return BookingDTO representing the updated booking.
     * @throws IllegalStateException If validation fails.
     */
    public BookingDTO updateBooking(BookingDTO bookingDTO) {
        validateBooking(bookingDTO); // Reuse validation logic

        // Convert DTO to entity and update
        Bookings booking = bookingMapper.toBookingEntity(bookingDTO);
        booking = bookingRepository.getEntityManager().merge(booking);
        return bookingMapper.toBookingDTO(booking);
    }

    /**
     * Delete a booking by its ID.
     * @param id Booking ID
     */
    public void deleteBooking(Long id) {
        Bookings booking = bookingRepository.findById(id);
        if (booking != null) {
            bookingRepository.delete(booking);
        }
    }

    /**
     * Validate the booking details.
     * @param bookingDTO Booking data transfer object containing details to validate.
     * @throws IllegalStateException If any validation check fails.
     */
    private void validateBooking(BookingDTO bookingDTO) {
        // Check if the seat is already booked
        if (seatRepository.isSeatBooked(bookingDTO.getSeatId())) {
            throw new IllegalStateException("The seat is already booked.");
        }

        // Check if the user has already booked the same seat
        if (bookingRepository.hasUserAlreadyBookedSeat(bookingDTO.getUserId(), bookingDTO.getSeatId())) {
            throw new IllegalStateException("User has already booked this seat.");
        }

        // Check if booking is allowed within a time frame (e.g., cannot book in the past)
        if (bookingDTO.getBookingTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot book for a past time.");
        }

        // Check if the user has exceeded the booking limit for the day (e.g., max 5 bookings/day)
        long bookingsToday = bookingRepository.countUserBookingsForToday(bookingDTO.getUserId());
        if (bookingsToday >= 5) {
            throw new IllegalStateException("User has exceeded the maximum booking limit for today.");
        }

        // Check if the payment status is valid
        if (!isValidPaymentStatus(bookingDTO.getPaymentStatus())) {
            throw new IllegalStateException("Invalid payment status.");
        }
    }

    /**
     * Validate the payment status.
     * @param paymentStatus Payment status to validate.
     * @return True if valid, false otherwise.
     */
    private boolean isValidPaymentStatus(String paymentStatus) {
        return paymentStatus != null && (paymentStatus.equals("PENDING") || paymentStatus.equals("PAID") || paymentStatus.equals("CANCELLED"));
    }
}
