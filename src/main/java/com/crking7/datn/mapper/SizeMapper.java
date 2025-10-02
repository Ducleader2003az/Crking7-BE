package com.crking7.datn.mapper;

import com.crking7.datn.web.dto.request.SizeUDRequest;
import com.crking7.datn.web.dto.response.SizeResponse;
import com.crking7.datn.models.Size;
import com.crking7.datn.web.dto.request.SizeRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(uses = ProductMapper.class)
public interface SizeMapper {

    SizeResponse mapModelToResponse(Size size);
    Size mapRequestToModel(SizeRequest sizeRequest);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModel(@MappingTarget Size size, SizeRequest sizeRequest);
    Size mapRequestToModel2(SizeUDRequest sizeRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModel2(@MappingTarget Size size, SizeUDRequest sizeRequest);

}
