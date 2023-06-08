package com.ape.business.abstracts;

import com.ape.dto.ProductDTO;
import com.ape.dto.request.ProductRequest;
import com.ape.dto.request.ProductUpdateRequest;
import com.ape.entity.enums.ProductStatus;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    PageImpl<ProductDTO> findAllWithQueryAndPage(String query, List<Long> categoryId, List<Long> brandId, Integer minPrice, Integer maxPrice, ProductStatus status, Pageable pageable);
    ProductDTO getProductDTOById(Long id);
    ProductDTO saveProduct(ProductRequest productRequest);
    ProductDTO updateProduct(Long id, ProductUpdateRequest productUpdateRequest);
    ProductDTO removeById(Long id);
    void removeImageById(String id);
}
