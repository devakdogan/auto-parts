package com.ape.entity.dao;

import com.ape.entity.concrete.AddressEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressDao extends JpaRepository<AddressEntity,Long> {

    @EntityGraph(attributePaths = {"user"})
    List<AddressEntity> findAllByUserId(Long id);
}
