package com.crking7.datn.mapper;

import com.crking7.datn.models.*;
import com.crking7.datn.models.dtos.ProductDto;
import com.crking7.datn.web.dto.request.ProductUDRequest;
import com.crking7.datn.web.dto.response.*;
import com.crking7.datn.web.dto.request.ProductRequest;
import org.mapstruct.*;

@Mapper
public interface ProductMapper{
    //map danh sách ProductResponse với danh sách Product
    @Mapping(target = "colors", source = "colors")
    @Mapping(target = "images", source = "productImages")
    @Mapping(target = "category",source = "productCategory.id")
    @Mapping(target = "author",source = "productAuthor.id")
    @Mapping(target = "sale",source = "sale.id")
    ProductResponse mapModelToResponse(Product product);

    @Mapping(target = "images", source = "productImages")
    @Mapping(target = "colors", source = "colors")
    @Mapping(target = "category",source = "productCategory.id")
    @Mapping(target = "author",source = "productAuthor.id")
    @Mapping(target = "sale",source = "sale.id")
    ProductDto mapModelToResponseDTO(Product product);

    @Mapping(target = "sizes", source = "sizes")
    ColorResponse mapColorToColorResponse(Color color);

    SizeResponse mapSizeToSizeResponse(Size size);

    ProductImageResponse mapImageToImageResponse(ProductImage productImage);

    @Mapping(target = "productCategory.id", source = "categoryId")
    @Mapping(target = "productAuthor.id", source = "userId")
    Product mapRequestedToModel(ProductRequest productRequest);

    //Update
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "productCategory.id", source = "categoryId")
    @Mapping(target = "productAuthor.id", source = "userId")
    void updateModel(@MappingTarget Product product, ProductRequest productRequest);
    //
    CategoryResponse mapModelToResponse(Category category);
    @Mapping(target = "roles",source = "roles")
    UserResponse mapModelToResponse(User user);
    //Update Tets
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModel2(@MappingTarget Product product, ProductUDRequest productRequest);
}
