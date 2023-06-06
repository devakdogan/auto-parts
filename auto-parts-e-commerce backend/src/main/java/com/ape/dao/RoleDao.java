package com.ape.dao;

import com.ape.entity.Role;
import com.ape.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleDao extends JpaRepository<Role,Long> {

    Optional<Role> findByRoleName(RoleType roleType);
}
