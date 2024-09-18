package com.example.mapper;

import com.example.entity.Showtimes;
import com.example.dto.ShowtimesDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShowtimesMapper {
    ShowtimesMapper INSTANCE = Mappers.getMapper(ShowtimesMapper.class);
    
    @Mapping(source = "id", target = "id" )
    @Mapping(source = "movieId", target = "movieId")
    @Mapping(source = "showtime", target = "showtime")

    ShowtimesDTO toDTO(Showtimes showtimes);
    Showtimes toEntity(ShowtimesDTO showtimesDTO);
}
