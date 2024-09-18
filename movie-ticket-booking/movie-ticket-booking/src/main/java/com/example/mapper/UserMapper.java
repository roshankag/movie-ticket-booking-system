package com.example.mapper;


import com.example.dto.UserDTO;
import com.example.entity.Users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    
    @Mapping(source = "id", target = "id" )
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")

    UserDTO toDTO(Users user);
    Users toEntity(UserDTO userDTO);
}

