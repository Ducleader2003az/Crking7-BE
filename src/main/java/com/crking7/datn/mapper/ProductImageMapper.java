package com.crking7.datn.mapper;

import com.crking7.datn.models.ProductImage;
import com.crking7.datn.web.dto.response.ProductImageResponse;
import org.mapstruct.Mapper;

@Mapper
public interface ProductImageMapper {
	ProductImageResponse mapModelToResponse(ProductImage productImage);
}
