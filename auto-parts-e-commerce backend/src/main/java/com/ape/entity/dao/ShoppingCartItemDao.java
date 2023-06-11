package com.ape.entity.dao;

import com.ape.entity.concrete.ShoppingCartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartItemDao extends JpaRepository<ShoppingCartItemEntity,Long> {
    ShoppingCartItemEntity findByProductIdAndShoppingCartCartUUID(Long id, String cartUUID);
}
