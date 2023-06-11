package com.ape.entity.dao;

import com.ape.entity.concrete.ShoppingCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ShoppingCartDao extends JpaRepository<ShoppingCartEntity,Long> {
    Optional<ShoppingCartEntity> findByCartUUID(String uuid);
}
