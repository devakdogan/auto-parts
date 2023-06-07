package com.ape.dao;

import com.ape.entity.ShoppingCartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartItemDao extends JpaRepository<ShoppingCartItemEntity,Long> {
}
