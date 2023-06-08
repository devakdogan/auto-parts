package com.ape.mapper;

import com.ape.dto.ShoppingCartItemDTO;
import com.ape.entity.ImageFileEntity;
import com.ape.entity.ProductEntity;
import com.ape.entity.ShoppingCartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ShoppingCartItemMapper {

    @Mapping(source = "product", target = "productId", qualifiedByName = "getProductId")
    @Mapping(source = "product", target = "title", qualifiedByName = "getProductTitle")
    @Mapping(source = "product", target = "imageId", qualifiedByName = "getProductImageId")
    @Mapping(source = "product", target = "unitPrice", qualifiedByName = "getProductPrice")
    @Mapping(source = "product", target = "discount", qualifiedByName = "getProductDiscount")
    @Mapping(source = "product",target = "discountedPrice", qualifiedByName = "getDiscountedPrice")
    @Mapping(source = "product",target = "tax",qualifiedByName = "getTaxRate")
    @Mapping(source = "product",target = "stockAmount",qualifiedByName = "getStockAmount")
    ShoppingCartItemDTO shoppingCartItemToShoppingCartItemDTO(ShoppingCartItemEntity shoppingCartItem);

    List<ShoppingCartItemDTO> shoppingCartItemToShoppingCartItemDTOAsList(List<ShoppingCartItemEntity> shoppingCartItemList);

    @Named("getProductId")
    public static Long getProductId(ProductEntity product){
        return product.getId();
    }

    @Named("getProductTitle")
    public static String getProductTitle(ProductEntity product){
        return product.getTitle();
    }

    @Named("getProductImageId")
    public static UUID getProductImageId(ProductEntity product){
        return product.getImages().stream().filter(ImageFileEntity::isShowcase).map(ImageFileEntity::getId).findFirst().orElse(null);
    }

    @Named("getProductPrice")
    public static Double getProductPrice(ProductEntity product){
        return product.getPrice();
    }

    @Named("getProductDiscount")
    public static Integer getProductDiscount(ProductEntity product){
        return product.getDiscount();
    }

    @Named("getDiscountedPrice")
    public static Double getDiscountedPrice(ProductEntity product){
        return product.getDiscountedPrice();
    }

    @Named("getTaxRate")
    public static Double getTaxRate(ProductEntity product){
        return product.getTax();
    }
    @Named("getStockAmount")
    public static Integer getStockAmount(ProductEntity product){
        return product.getStockAmount();
    }

}