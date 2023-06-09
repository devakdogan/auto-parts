package com.ape.entity.dao;

import com.ape.entity.concrete.RoleEntity;
import com.ape.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleDao extends JpaRepository<RoleEntity,Long> {

    Optional<RoleEntity> findByRoleName(RoleType roleType);
}
