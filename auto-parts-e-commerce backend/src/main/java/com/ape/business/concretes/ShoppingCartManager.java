package com.ape.business.concretes;

import com.ape.business.abstracts.ShoppingCartService;
import com.ape.dao.ShoppingCartDao;
import com.ape.dto.ShoppingCartDTO;
import com.ape.dto.ShoppingCartItemDTO;
import com.ape.dto.request.ShoppingCartRequest;
import com.ape.entity.ShoppingCartEntity;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.ShoppingCartMapper;
import com.ape.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartManager implements ShoppingCartService {
    private final ShoppingCartDao shoppingCartDao;
    private final ShoppingCartMapper shoppingCartMapper;
    @Override
    public ShoppingCartEntity findCartByUUID(String cartUUID) {
        return shoppingCartDao.findByCartUUID(cartUUID).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,cartUUID)));
    }

    @Override
    @Transactional
    public ShoppingCartItemDTO createCartItem(String cartUUID, ShoppingCartRequest shoppingCartRequest) {
        return null;
    }

    @Override
    @Transactional
    public ShoppingCartItemDTO removeCartItem(String cartUUID, Long productId) {
        return null;
    }

    @Override
    @Transactional
    public void cleanShoppingCart(String cartUUID) {

    }

    @Override
    @Transactional
    public ShoppingCartItemDTO changeItemQuantity(String cartUUID, Long productId) {
        return null;
    }

    @Override
    public ShoppingCartDTO getShoppingCart(String cartUUID) {
        ShoppingCartEntity shoppingCart;
        if (!cartUUID.isEmpty()){
            shoppingCart = findCartByUUID(cartUUID);
        } else{
            shoppingCart = new ShoppingCartEntity();
            shoppingCart.setCartUUID(UUID.randomUUID().toString());
            shoppingCartDao.save(shoppingCart);

        }
        return shoppingCartMapper.shoppingCartToShoppingCartDTO(shoppingCart);
    }

    @Override
    @Transactional
    public void save(ShoppingCartEntity shoppingCartEntity) {

    }
}
