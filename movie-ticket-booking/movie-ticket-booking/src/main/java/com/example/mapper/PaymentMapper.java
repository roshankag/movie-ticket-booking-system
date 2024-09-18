package com.example.mapper;

import com.example.dto.PaymentDTO;
import com.example.entity.Payments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);
    
    @Mapping(source = "id", target = "id" )
    @Mapping(source = "bookingId", target = "bookingId")
    @Mapping(source = "paymentAmount", target = "paymentAmount")
    @Mapping(source = "paymentTime", target = "paymentTime")
    @Mapping(source = "paymentStatus", target = "paymentStatus")
    

    PaymentDTO toDTO(Payments payment);
    Payments toEntity(PaymentDTO paymentDTO);
}
