package com.ape.entity.dao;

import com.ape.entity.concrete.UserEntity;
import com.ape.entity.enums.RoleType;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserDao extends JpaRepository<UserEntity,Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<UserEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    @NonNull
    List<UserEntity> findAll();

    @EntityGraph(attributePaths = {"roles"})
    @NonNull
    Optional<UserEntity> findById(Long id);

    @EntityGraph(attributePaths = {"id","shoppingCart"})
    Optional<UserEntity> findUserById(Long id);

    @Query("select u from UserEntity u inner join u.roles r where  r.roleName = :role")
    List<UserEntity> findByRole(@Param("role") RoleType roleType);
}
