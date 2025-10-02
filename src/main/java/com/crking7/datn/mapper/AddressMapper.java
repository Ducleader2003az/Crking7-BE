package com.crking7.datn.mapper;

import com.crking7.datn.models.Address;
import com.crking7.datn.web.dto.request.AddressRequest;
import com.crking7.datn.web.dto.response.AddressResponse;
import org.mapstruct.*;

@Mapper
public interface AddressMapper {
    AddressResponse mapToResponse(Address address);

    @Mapping(target = "user.id", source = "userId")
    Address mapToModel(AddressRequest addressRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModel(@MappingTarget Address address, AddressRequest addressRequest);
}
