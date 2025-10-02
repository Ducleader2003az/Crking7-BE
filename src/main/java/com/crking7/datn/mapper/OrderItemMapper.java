package com.crking7.datn.mapper;

import com.crking7.datn.models.OrderItem;
import com.crking7.datn.web.dto.request.OrderItemRequest;
import com.crking7.datn.web.dto.response.OrderItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrderItemMapper {
    OrderItemResponse mapToResponse(OrderItem orderItem);

    OrderItem mapToModel(OrderItemRequest orderItemRequest);
}
