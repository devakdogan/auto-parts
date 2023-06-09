package com.ape.entity.dao;

import com.ape.entity.concrete.ShoppingCartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartItemDao extends JpaRepository<ShoppingCartItemEntity,Long> {
}
