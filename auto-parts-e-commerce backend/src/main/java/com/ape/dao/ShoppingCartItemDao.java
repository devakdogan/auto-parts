package com.ape.dao;

import com.ape.entity.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartItemDao extends JpaRepository<ShoppingCartItem,Long> {
}
