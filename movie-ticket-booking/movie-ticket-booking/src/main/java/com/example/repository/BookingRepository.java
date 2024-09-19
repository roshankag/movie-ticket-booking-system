package com.example.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.entity.Bookings;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;

@ApplicationScoped
public class BookingRepository implements PanacheRepositoryBase<Bookings, Long> {

    // Custom method to check if a user has already booked the same seat
    public boolean hasUserAlreadyBookedSeat(Long userId, Long seatId) {
        String query = "SELECT COUNT(b) FROM Bookings b WHERE b.userId = :userId AND b.seatId = :seatId";
        TypedQuery<Long> typedQuery = getEntityManager().createQuery(query, Long.class)
                .setParameter("userId", userId)
                .setParameter("seatId", seatId);
        
        Long count = typedQuery.getSingleResult();
        return count > 0;
    }

    // Custom method to count the number of bookings a user has made for the day
    public long countUserBookingsForToday(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        String query = "SELECT COUNT(b) FROM Bookings b WHERE b.userId = :userId AND b.bookingTime BETWEEN :startOfDay AND :endOfDay";
        TypedQuery<Long> typedQuery = getEntityManager().createQuery(query, Long.class)
                .setParameter("userId", userId)
                .setParameter("startOfDay", startOfDay)
                .setParameter("endOfDay", endOfDay);
        
        return typedQuery.getSingleResult();
    }
    
}
