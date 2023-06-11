package com.ape.business.abstracts;

import com.ape.entity.dto.ShoppingCartDTO;
import com.ape.entity.dto.ShoppingCartItemDTO;
import com.ape.entity.dto.request.ShoppingCartRequest;
import com.ape.entity.concrete.ShoppingCartEntity;

public interface ShoppingCartService {
    ShoppingCartEntity findCartByUUID(String cartUUID);
    ShoppingCartItemDTO createCartItem(String cartUUID, ShoppingCartRequest shoppingCartRequest);
    ShoppingCartItemDTO removeCartItem(String cartUUID, Long productId);
    void cleanShoppingCart(String cartUUID);
    ShoppingCartItemDTO changeItemQuantity(String cartUUID, Long productId, String op);
    ShoppingCartDTO getShoppingCart(String cartUUID);
    void save(ShoppingCartEntity shoppingCartEntity);
}
