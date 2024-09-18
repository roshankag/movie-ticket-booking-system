package com.example.mapper;

import com.example.entity.Bookings;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.dto.BookingDTO;

@Mapper
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(source = "id", target = "id" )
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "seatId", target = "seatId")
    @Mapping(source = "bookingTime", target = "bookingTime")
    @Mapping(source = "paymentStatus", target = "paymentStatus")
    
    BookingDTO toBookingDTO(Bookings booking);

    Bookings toBookingEntity(BookingDTO bookingDTO);
}
