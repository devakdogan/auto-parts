package com.ape.entity.dao;

import com.ape.entity.concrete.CreditCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditCardDao extends JpaRepository<CreditCardEntity,Long> {
    List<CreditCardEntity> findByUserId(Long id);
}
