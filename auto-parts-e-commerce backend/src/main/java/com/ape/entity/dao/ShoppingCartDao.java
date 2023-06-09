package com.ape.entity.dao;

import com.ape.entity.concrete.ShoppingCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartDao extends JpaRepository<ShoppingCartEntity,Long> {
    Optional<ShoppingCartEntity> findByCartUUID(String uuid);
}
