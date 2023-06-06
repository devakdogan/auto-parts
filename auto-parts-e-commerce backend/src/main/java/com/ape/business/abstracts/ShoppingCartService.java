package com.ape.business.abstracts;

import com.ape.dto.request.ShoppingCartRequest;
import com.ape.dto.response.ShoppingCartDTO;
import com.ape.dto.response.ShoppingCartItemDTO;
import com.ape.entity.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart findCartByUUID(String cartUUID);
    ShoppingCartItemDTO createCartItem(String cartUUID, ShoppingCartRequest shoppingCartRequest);
    ShoppingCartItemDTO removeCartItem(String cartUUID, Long productId);
    void cleanShoppingCart(String cartUUID);
    ShoppingCartItemDTO changeItemQuantity(String cartUUID, Long productId);
    ShoppingCartDTO getShoppingCart(String cartUUID);
    void save(ShoppingCart shoppingCart);
}
