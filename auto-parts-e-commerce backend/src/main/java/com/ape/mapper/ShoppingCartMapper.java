package com.ape.mapper;


import com.ape.dto.ShoppingCartDTO;
import com.ape.entity.ShoppingCartEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = ShoppingCartItemMapper.class)
public interface ShoppingCartMapper {


  @Mapping(source = "shoppingCartItems", target = "shoppingCartItemDTO")
  ShoppingCartDTO shoppingCartToShoppingCartDTO(ShoppingCartEntity shoppingCart);

  List <ShoppingCartDTO> map(List<ShoppingCartEntity> shoppingCarts);

}


