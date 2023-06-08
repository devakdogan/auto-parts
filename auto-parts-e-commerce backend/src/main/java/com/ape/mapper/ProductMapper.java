package com.ape.mapper;

import com.ape.dto.ProductDTO;
import com.ape.dto.request.ProductRequest;
import com.ape.dto.request.ProductUpdateRequest;
import com.ape.entity.BrandEntity;
import com.ape.entity.CategoryEntity;
import com.ape.entity.ImageFileEntity;
import com.ape.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {BrandMapper.class,CategoryMapper.class})
public interface ProductMapper {

    ProductDTO entityToDTO(ProductEntity product);

    List<ProductDTO> entityListToDTOList(List<ProductEntity> productList);

    @Mapping(target="brand", ignore=true)
    @Mapping(target="category", ignore=true)
    @Mapping(target="sku", ignore=true)
    @Mapping(target = "slug",ignore = true)
    @Mapping(target = "discountedPrice",ignore = true)
    ProductEntity productRequestToProduct(ProductRequest productRequest);

    @Mapping(target="brand", ignore=true)
    @Mapping(target="category", ignore=true)
    @Mapping(target="sku", ignore=true)
    @Mapping(target="images", ignore=true)
    ProductEntity productUpdateRequestToProduct(ProductUpdateRequest productUpdateRequest);

    @Named("getProductId")
    public static Long getProductId(ProductEntity product) {
        return product.getId();
    }

    @Named("getBrandId")
    public static Long getBrandId(BrandEntity brand) {
        return brand.getId();
    }
    @Named("getCategoryId")
    public static Long getCategoryId(CategoryEntity category) {
        return category.getId();
    }

    @Named("getImageAsString")
    public static Set<String> getImageId(Set<ImageFileEntity> imageFiles){
        Set<String> images;
        images = imageFiles.stream().map(ImageFileEntity::getId).collect(Collectors.toSet());
        return images;
    }
}
