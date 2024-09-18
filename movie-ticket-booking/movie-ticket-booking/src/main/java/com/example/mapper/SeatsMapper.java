package com.example.mapper;

import com.example.dto.SeatsDTO;
import com.example.entity.Seats;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SeatsMapper {

    SeatsMapper INSTANCE = Mappers.getMapper(SeatsMapper.class);
    
    @Mapping(source = "id", target = "id" )
    @Mapping(source = "seatNumber", target = "seatNumber")
    @Mapping(source = "showtimeId", target = "showtimeId")
    @Mapping(source = "isBooked", target = "isBooked")

    SeatsDTO toDTO(Seats seat);
    Seats toEntity(SeatsDTO seatDTO);
}
