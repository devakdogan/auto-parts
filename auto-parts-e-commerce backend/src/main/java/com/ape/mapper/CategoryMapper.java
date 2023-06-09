package com.ape.mapper;

import com.ape.entity.dto.CategoryDTO;
import com.ape.entity.dto.ProductCategoryDTO;
import com.ape.entity.concrete.CategoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO entityToDTO(CategoryEntity category);

    ProductCategoryDTO entityToProductCategoryDTO(CategoryEntity category);

    List<CategoryDTO> categoryListToCategoryDTOList(List<CategoryEntity> categories);


}
