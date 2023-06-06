package com.ape.dao;

import com.ape.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShoppingCartDao extends JpaRepository<ShoppingCart,Long> {
    Optional<ShoppingCart> findByCartUUID(String uuid);

    @Modifying
    @Query("DELETE FROM ShoppingCart sc WHERE sc.id NOT IN (SELECT u.shoppingCart.id FROM User u)")
    void deleteAllShoppingCartsWithoutUser();
}
