package com.crking7.datn.mapper;

import com.crking7.datn.models.Category;
import com.crking7.datn.models.dtos.CategoryDto;
import com.crking7.datn.web.dto.request.CategoryRequest;
import com.crking7.datn.web.dto.response.CategoryResponse;
import org.mapstruct.*;

import java.util.List;
@Mapper
public interface CategoryMapper {
		//Map model to response
		@Mapping(target = "categoryParent",source = "parentCategory.id")
		CategoryResponse mapModelToResponse(Category category);

		// mapper one model to dto
		CategoryDto mapModelToDTO(Category category);

		// mapper list model to dto
		List<CategoryDto> mapModelToDTOs(List<Category> categories);

		// mapper one dto to model
		Category mapDTOToModel(CategoryDto categoryDto);

		Category mapRequestToModel(CategoryRequest categoryRequest);

		// mapper list dto to model
		List<Category> mapDTOToModels(List<CategoryDto> categoryDtos);

		@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
		void updateModel(@MappingTarget Category category, CategoryRequest categoryRequest);

}
