package com.ape.entity.dao;

import com.ape.entity.concrete.AddressEntity;
import com.ape.entity.concrete.OrderEntity;
import com.ape.entity.concrete.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDao extends JpaRepository<OrderEntity,Long> {
    @EntityGraph(attributePaths = "id")
    Optional<OrderEntity> findByIdAndUserId(Long orderId, Long userId);

    Page<OrderEntity> findAllByUserId(Long id, Pageable pageable);

    boolean existsByInvoiceAddress(AddressEntity userAddress);

    boolean existsByShippingAddress(AddressEntity userAddress);
}
