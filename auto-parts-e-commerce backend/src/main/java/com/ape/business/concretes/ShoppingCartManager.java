package com.ape.business.concretes;

import com.ape.business.abstracts.ShoppingCartService;
import com.ape.dao.ShoppingCartDao;
import com.ape.dto.request.ShoppingCartRequest;
import com.ape.dto.response.ShoppingCartDTO;
import com.ape.dto.response.ShoppingCartItemDTO;
import com.ape.entity.ShoppingCartEntity;
import com.ape.exception.ResourceNotFoundException;
import com.ape.utility.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartManager implements ShoppingCartService {
    private final ShoppingCartDao shoppingCartDao;
    @Override
    public ShoppingCartEntity findCartByUUID(String cartUUID) {
        return shoppingCartDao.findByCartUUID(cartUUID).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,cartUUID)));
    }

    @Override
    public ShoppingCartItemDTO createCartItem(String cartUUID, ShoppingCartRequest shoppingCartRequest) {
        return null;
    }

    @Override
    public ShoppingCartItemDTO removeCartItem(String cartUUID, Long productId) {
        return null;
    }

    @Override
    public void cleanShoppingCart(String cartUUID) {

    }

    @Override
    public ShoppingCartItemDTO changeItemQuantity(String cartUUID, Long productId) {
        return null;
    }

    @Override
    public ShoppingCartDTO getShoppingCart(String cartUUID) {
        return null;
    }

    @Override
    public void save(ShoppingCartEntity shoppingCartEntity) {

    }
}
