package com.ape.entity.dao;

import com.ape.entity.concrete.BrandEntity;
import com.ape.entity.enums.BrandStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BrandDao extends JpaRepository<BrandEntity,Long> {

    boolean existsByName(String name);
    @Query("SELECT b from BrandEntity b WHERE b.status=:status and b.id=:id")
    Optional<BrandEntity> getBrandByStatusPublishedAndId(BrandStatus status, Long id);

    @Query("SELECT count(b) from BrandEntity b join b.image img where img.id=:id")
    Integer findBrandByImageId(@Param("id")String id);

    boolean existsByImageId(String id);
}
