package com.ape.business.abstracts;

import com.ape.dto.CategoryDTO;
import com.ape.dto.request.CategoryRequest;
import com.ape.dto.request.CategoryUpdateRequest;
import com.ape.entity.CategoryEntity;
import com.ape.entity.enums.CategoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    Page<CategoryDTO> getAllCategoriesWithFilterAndPage(String query, CategoryStatus status, Pageable pageable);
    List<CategoryDTO> getAllCategoryList();
    CategoryDTO saveCategory(CategoryRequest categoryRequest);
    CategoryDTO updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest);
    CategoryDTO removeById(Long id);
    CategoryEntity getCategoryById(Long id);
}
