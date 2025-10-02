package com.crking7.datn.mapper;

import com.crking7.datn.models.Article;
import com.crking7.datn.models.ArticleImage;
import com.crking7.datn.models.Category;
import com.crking7.datn.web.dto.request.ArticleRequest;
import com.crking7.datn.web.dto.request.ArticleUDRequest;
import com.crking7.datn.web.dto.request.CategoryRequest;
import com.crking7.datn.web.dto.response.ArticleImageResponse;
import com.crking7.datn.web.dto.response.ArticleResponse;
import org.mapstruct.*;

@Mapper
public interface ArticleMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "categoryId", source = "category.id")
    ArticleResponse mapToResponse(Article article);

    ArticleImageResponse mapImageToResponse(ArticleImage articleImage);

    @Mapping(target = "category.id", source = "categoryId")
    @Mapping(target = "user.id", source = "userId")
    Article mapRequestToModel(ArticleRequest articleRequest);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModel(@MappingTarget Article article, ArticleRequest articleRequest);
}
