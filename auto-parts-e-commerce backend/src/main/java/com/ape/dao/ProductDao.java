package com.ape.dao;

import com.ape.entity.ProductEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDao extends JpaRepository<ProductEntity,Long> {
    Optional<ProductEntity> findProductById(Long productId);

    Boolean existsByBrandId(Long id);

    Boolean existsByCategoryId(Long id);

    @Query( "SELECT p FROM ProductEntity p JOIN p.images img WHERE img.id=:id")
    ProductEntity findProductByImageId(@Param("id") String id );

    @Query("SELECT p FROM OrderItemEntity oi INNER JOIN oi.product p WHERE p.id=:productId")
    List<ProductEntity> checkOrderItemsByID(@Param("productId") Long id);
}
