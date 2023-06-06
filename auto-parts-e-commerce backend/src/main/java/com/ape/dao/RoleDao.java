package com.ape.dao;

import com.ape.entity.Role;
import com.ape.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleDao extends JpaRepository<Role,Long> {

    Optional<Role> findByRoleName(RoleType roleType);
}
