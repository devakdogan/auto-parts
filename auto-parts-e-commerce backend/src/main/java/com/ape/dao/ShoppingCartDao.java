package com.ape.dao;

import com.ape.entity.ShoppingCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartDao extends JpaRepository<ShoppingCartEntity,Long> {
    Optional<ShoppingCartEntity> findByCartUUID(String uuid);
}
