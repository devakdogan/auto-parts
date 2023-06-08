package com.ape.business.abstracts;

import com.ape.dto.ShoppingCartDTO;
import com.ape.dto.ShoppingCartItemDTO;
import com.ape.dto.request.ShoppingCartRequest;
import com.ape.entity.ShoppingCartEntity;

public interface ShoppingCartService {
    ShoppingCartEntity findCartByUUID(String cartUUID);
    ShoppingCartItemDTO createCartItem(String cartUUID, ShoppingCartRequest shoppingCartRequest);
    ShoppingCartItemDTO removeCartItem(String cartUUID, Long productId);
    void cleanShoppingCart(String cartUUID);
    ShoppingCartItemDTO changeItemQuantity(String cartUUID, Long productId);
    ShoppingCartDTO getShoppingCart(String cartUUID);
    void save(ShoppingCartEntity shoppingCartEntity);
}
