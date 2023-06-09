package com.ape.business.abstracts;

import com.ape.entity.dto.BrandDTO;
import com.ape.entity.dto.request.BrandRequest;
import com.ape.entity.dto.request.BrandUpdateRequest;
import com.ape.entity.concrete.BrandEntity;
import com.ape.entity.enums.BrandStatus;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface BrandService {
    PageImpl<BrandDTO> getAllBrandsWithFilterAndPage(String query, BrandStatus status, Pageable pageable);
    BrandDTO getBrandById(Long id);
    BrandDTO createBrand(BrandRequest brandRequest);
    BrandDTO updateBrand(Long id, BrandUpdateRequest brandUpdateRequest);
    BrandDTO deleteBrandById(Long id);
    BrandEntity findBrandById(Long id);
}
