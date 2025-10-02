package com.crking7.datn.mapper;

import com.crking7.datn.web.dto.request.BannerRequest;
import com.crking7.datn.web.dto.response.BannerResponse;
import com.crking7.datn.models.Banner;
import org.mapstruct.*;

@Mapper
public interface BannerMapper {
	@Mapping(target = "categoryId", source = "category.id")
    BannerResponse mapModelToResponse(Banner banner);

	Banner mapRequestToModel(BannerRequest bannerRequest);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateModel(@MappingTarget Banner banner, BannerRequest bannerRequest);
}
