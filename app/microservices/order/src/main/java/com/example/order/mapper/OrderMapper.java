package com.example.order.mapper;

import com.example.order.dto.OrderResponseDTO;
import com.example.order.entity.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponseDTO toResponse(OrderEntity orderEntity) {
        return OrderResponseDTO.builder()
                .id(orderEntity.getId())
                .userId(orderEntity.getUserId())
                .productId(orderEntity.getProductId())
                .quantity(orderEntity.getQuantity())
                .status(orderEntity.getStatus())
                .createdAt(orderEntity.getCreatedAt())
                .build();
    }
}
