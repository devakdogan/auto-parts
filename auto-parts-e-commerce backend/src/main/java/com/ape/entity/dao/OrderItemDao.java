package com.ape.entity.dao;

import com.ape.entity.concrete.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemDao extends JpaRepository<OrderItemEntity,Long> {
}
