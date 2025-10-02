package com.crking7.datn.mapper;

import com.crking7.datn.models.Sale;
import com.crking7.datn.models.Size;
import com.crking7.datn.web.dto.request.SaleRequest;
import com.crking7.datn.web.dto.request.SizeRequest;
import com.crking7.datn.web.dto.request.SizeUDRequest;
import com.crking7.datn.web.dto.response.SaleResponse;
import com.crking7.datn.web.dto.response.SizeResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(uses = ProductMapper.class)
public interface SaleMapper {

    SaleResponse mapModelToResponse(Sale sale);
    Sale mapRequestToModel(SaleRequest saleRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModel(@MappingTarget Sale Sale, SaleRequest saleRequest);

}
