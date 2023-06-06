package com.ape.dao;

import com.ape.entity.User;
import com.ape.entity.enums.RoleType;
import com.ape.entity.enums.UserStatus;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User,Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    @NonNull
    List<User> findAll();

    @EntityGraph(attributePaths = {"roles","favoriteList"})
    @NonNull
    Optional<User> findById(Long id);

    @EntityGraph(attributePaths = {"id","shoppingCart"})
    Optional<User> findUserById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.status = :status WHERE u.email = :email")
    void enableUser(@Param("status") UserStatus status, @Param("email") String email);

    @Query("select u from User u inner join u.roles r where  r.roleName = :role")
    List<User> findByRole(@Param("role") RoleType roleType);
}
