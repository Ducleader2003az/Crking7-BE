package com.crking7.datn.mapper;

import com.crking7.datn.models.Address;
import com.crking7.datn.models.Category;
import com.crking7.datn.models.Orders;
import com.crking7.datn.web.dto.request.AddressRequest;
import com.crking7.datn.web.dto.request.CategoryRequest;
import com.crking7.datn.web.dto.request.OrdersRequest;
import com.crking7.datn.web.dto.response.OrdersResponse;
import org.mapstruct.*;

@Mapper(uses = OrderItemMapper.class)
public interface OrdersMapper {
    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "user", source = "user.id")
    OrdersResponse mapToResponse(Orders orders);

    Orders mapToModel(OrdersRequest ordersRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModel(@MappingTarget Orders orders, OrdersRequest ordersRequest);
}
