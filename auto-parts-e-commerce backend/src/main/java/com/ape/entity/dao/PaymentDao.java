package com.ape.entity.dao;

import com.ape.entity.concrete.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDao extends JpaRepository<PaymentEntity,Long> {
}
