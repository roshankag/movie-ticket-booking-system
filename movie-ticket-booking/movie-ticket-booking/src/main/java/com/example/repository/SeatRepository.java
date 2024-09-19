package com.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;

import com.example.entity.Seats;

@ApplicationScoped
public class SeatRepository implements PanacheRepositoryBase<Seats, Long> {
    // Add custom methods if needed
	
	 // Method to check if a seat is already booked
    public boolean isSeatBooked(Long seatId) {
        String query = "SELECT COUNT(b) FROM Bookings b WHERE b.seatId = :seatId";
        TypedQuery<Long> typedQuery = getEntityManager().createQuery(query, Long.class)
                .setParameter("seatId", seatId);
        
        Long count = typedQuery.getSingleResult();
        return count > 0; // If count > 0, the seat is already booked
    }
}
