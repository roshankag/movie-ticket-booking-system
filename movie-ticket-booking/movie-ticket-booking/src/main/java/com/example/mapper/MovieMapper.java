package com.example.mapper;

import com.example.entity.Movies;
import com.example.dto.MovieDTO; // Assuming you have a DTO class
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovieMapper {
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);
    
    @Mapping(source = "id", target = "id" )
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "releaseDate", target = "releaseDate")
    @Mapping(source = "durationMinutes", target = "durationMinutes")
    
    MovieDTO toDto(Movies movie);

    Movies toEntity(MovieDTO movieDTO);
}
