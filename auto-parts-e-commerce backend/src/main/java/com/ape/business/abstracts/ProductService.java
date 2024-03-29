package com.ape.business.abstracts;

import com.ape.entity.dto.ProductDTO;
import com.ape.entity.dto.request.ProductRequest;
import com.ape.entity.dto.request.ProductUpdateRequest;
import com.ape.entity.concrete.ProductEntity;
import com.ape.entity.enums.ProductStatus;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    PageImpl<ProductDTO> getAllWithQueryAndPage(String query, List<Long> categoryId, List<Long> brandId, Integer minPrice, Integer maxPrice, ProductStatus status, Pageable pageable);
    ProductDTO getProductById(Long id);
    ProductDTO saveProduct(ProductRequest productRequest);
    ProductDTO updateProduct(Long id, ProductUpdateRequest productUpdateRequest);
    ProductDTO removeById(Long id);
    void removeProductImageByImageId(String id);
    ProductEntity findProductById(Long id);
    long countProductRecords();
}
