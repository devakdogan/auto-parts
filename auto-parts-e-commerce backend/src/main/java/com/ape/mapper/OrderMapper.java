package com.ape.mapper;

import com.ape.entity.concrete.OrderEntity;
import com.ape.entity.concrete.UserEntity;
import com.ape.entity.dto.OrderDTO;
import com.ape.entity.dto.request.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.text.DecimalFormat;
import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, AddressMapper.class})
public interface OrderMapper {

    @Mapping(source = "orderItems",target = "orderItemsDTO")
    @Mapping(source = "user",target = "customer",qualifiedByName = "getUserFullName")
    OrderDTO entityToDTO (OrderEntity order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "status", ignore = true)
    OrderEntity orderRequestToOrderEntity (OrderRequest orderRequest);

    List<OrderDTO> map (List<OrderEntity> orderList);

    @Named("getUserFullName")
    public static String getUserId(UserEntity user){
        return user.getFirstName() + " " +user.getLastName();
    }


}