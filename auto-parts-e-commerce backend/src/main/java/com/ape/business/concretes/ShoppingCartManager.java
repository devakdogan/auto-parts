package com.ape.business.concretes;

import com.ape.business.abstracts.ShoppingCartService;
import com.ape.entity.concrete.ProductEntity;
import com.ape.entity.concrete.ShoppingCartItemEntity;
import com.ape.entity.dao.ShoppingCartDao;
import com.ape.entity.dao.ShoppingCartItemDao;
import com.ape.entity.dto.ShoppingCartDTO;
import com.ape.entity.dto.ShoppingCartItemDTO;
import com.ape.entity.dto.request.ShoppingCartRequest;
import com.ape.entity.concrete.ShoppingCartEntity;
import com.ape.exception.BadRequestException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.ShoppingCartItemMapper;
import com.ape.mapper.ShoppingCartMapper;
import com.ape.exception.ErrorMessage;
import com.ape.utility.DiscountCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartManager implements ShoppingCartService {
    private final ShoppingCartDao shoppingCartDao;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartItemMapper shoppingCartItemMapper;
    private final ProductManager productManager;
    private final ShoppingCartItemDao shoppingCartItemDao;
    private final DiscountCalculator discountCalculator;

    @Override
    public ShoppingCartEntity findCartByUUID(String cartUUID) {
        return shoppingCartDao.findByCartUUID(cartUUID).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,cartUUID)));
    }

    @Override
    @Transactional
    public ShoppingCartItemDTO createCartItem(String cartUUID, ShoppingCartRequest shoppingCartRequest) {

        ShoppingCartEntity shoppingCart = findCartByUUID(cartUUID);

        Double totalPrice;

        ProductEntity product = productManager.findProductById(shoppingCartRequest.getProductId());
        ShoppingCartItemEntity foundItem = shoppingCartItemDao.findByProductIdAndShoppingCartCartUUID(product.getId(), shoppingCart.getCartUUID());
        ShoppingCartItemEntity shoppingCartItem = null;
        if (shoppingCartRequest.getQuantity() > product.getStockAmount()){
            throw new BadRequestException(String.format(ErrorMessage.PRODUCT_OUT_OF_STOCK_MESSAGE,product.getId()));
        }
        if (shoppingCart.getShoppingCartItems().size()>0 && shoppingCart.getShoppingCartItems().contains(foundItem)) {
            if (shoppingCartRequest.getQuantity() > foundItem.getProduct().getStockAmount()){
                throw new BadRequestException(String.format(ErrorMessage.PRODUCT_OUT_OF_STOCK_MESSAGE,product.getId()));
            }
            Integer quantity = foundItem.getQuantity() + shoppingCartRequest.getQuantity();
            foundItem.setQuantity(quantity);
            totalPrice = quantity*product.getPrice();
            foundItem.setTotalPrice(totalPrice);
            shoppingCartItemDao.save(foundItem);
            shoppingCart.setGrandTotal(shoppingCart.getGrandTotal()+(shoppingCartRequest.getQuantity()*foundItem.getProduct().getPrice()));
            shoppingCartItem = foundItem;
            shoppingCartItem.setUpdateAt(LocalDateTime.now());
        } else{
            shoppingCartItem = new ShoppingCartItemEntity();
            shoppingCartItem.setProduct(product);
            shoppingCartItem.setQuantity(shoppingCartRequest.getQuantity());
            shoppingCartItem.setShoppingCart(shoppingCart);
            totalPrice = shoppingCartRequest.getQuantity()*product.getPrice();
            shoppingCartItem.setTotalPrice(totalPrice);
            shoppingCartItemDao.save(shoppingCartItem);
            shoppingCart.getShoppingCartItems().add(shoppingCartItem);
            shoppingCart.setGrandTotal(shoppingCart.getGrandTotal()+totalPrice);
        }

        shoppingCartDao.save(shoppingCart);
        return shoppingCartItemMapper.entityToDTO(shoppingCartItem);
    }

    @Override
    @Transactional
    public ShoppingCartItemDTO removeCartItem(String cartUUID, Long productId) {
        ShoppingCartEntity shoppingCart = shoppingCartDao.findByCartUUID(cartUUID).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE));
        ShoppingCartItemEntity foundItem = shoppingCartItemDao.findByProductIdAndShoppingCartCartUUID(productId,
                cartUUID);
        shoppingCart.setGrandTotal(shoppingCart.getGrandTotal()-foundItem.getTotalPrice());
        shoppingCartItemDao.delete(foundItem);
        shoppingCartDao.save(shoppingCart);
        return shoppingCartItemMapper.entityToDTO(foundItem);
    }

    @Override
    @Transactional
    public void cleanShoppingCart(String cartUUID) {

    }

    @Override
    @Transactional
    public ShoppingCartItemDTO changeItemQuantity(String cartUUID, Long productId, String op) {
        ShoppingCartEntity shoppingCart = shoppingCartDao.findByCartUUID(cartUUID).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE));
        ProductEntity product = productManager.findProductById(productId);
        ShoppingCartItemEntity foundItem = shoppingCartItemDao.findByProductIdAndShoppingCartCartUUID(product.getId(),shoppingCart.getCartUUID());
        switch (op){
            case "increase":
                foundItem.setQuantity(foundItem.getQuantity()+1);
                shoppingCart.setGrandTotal(shoppingCart.getGrandTotal()+foundItem.getProduct().getPrice());
                break;
            case "decrease":
                foundItem.setQuantity(foundItem.getQuantity()-1);
                shoppingCart.setGrandTotal(shoppingCart.getGrandTotal()-foundItem.getProduct().getPrice());
                break;
        }
        Double totalPrice = discountCalculator.totalPriceWithDiscountCalculate(foundItem.getQuantity(), product.getPrice(), product.getDiscount());
        foundItem.setTotalPrice(totalPrice);
        foundItem.setUpdateAt(LocalDateTime.now());
        shoppingCartItemDao.save(foundItem);
        save(shoppingCart);

        return shoppingCartItemMapper.entityToDTO(foundItem);
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
    public void save(ShoppingCartEntity shoppingCart) {
    shoppingCartDao.save(shoppingCart);
    }
}
