package com.ape.mapper;

import com.ape.entity.concrete.ImageFileEntity;
import com.ape.entity.concrete.OrderItemEntity;
import com.ape.entity.concrete.ProductEntity;
import com.ape.entity.dto.OrderItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.text.DecimalFormat;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    DecimalFormat df = new DecimalFormat("#.##");

    @Mapping(source = "product",target = "productId",qualifiedByName = "getProductId")
    @Mapping(source = "product",target = "imageId",qualifiedByName = "getImageId")
    @Mapping(source = "product",target = "title",qualifiedByName = "getTitleOfProduct")
    @Mapping(target = "tax",expression = "java(orderItemToTax(orderItem))")
    @Mapping(target = "discount",expression = "java(orderItemToDiscount(orderItem))")
    OrderItemDTO orderItemToOrderItemDTO(OrderItemEntity orderItem);

    List<OrderItemDTO> orderItemsToOrderItemsDTO(List<OrderItemEntity> orderItems);

    @Named("getProductId")
    public static Long getProductId(ProductEntity product){
        return product.getId();
    }

    @Named("getImageId")
    public static String getImageId(ProductEntity product){
       return product.getImages().stream().filter(ImageFileEntity::isShowcase).map(ImageFileEntity::getId).findFirst().orElse(null);
    }

    @Named("getTitleOfProduct")
    public static String getTitleOfProduct(ProductEntity product){
        return product.getTitle();
    }

    public default double orderItemToTax(OrderItemEntity orderItem) {
        return Double.parseDouble(df.format(((((orderItem.getUnitPrice() * orderItem.getQuantity()) * (100-orderItem.getDiscount())) / 100) * orderItem.getTax()) / 100).replaceAll(",","."));
    }

    public default double orderItemToDiscount(OrderItemEntity orderItem) {
        return Double.parseDouble(df.format(((orderItem.getUnitPrice() * orderItem.getQuantity()) * orderItem.getDiscount()) / 100).replaceAll(",","."));
    }
}
