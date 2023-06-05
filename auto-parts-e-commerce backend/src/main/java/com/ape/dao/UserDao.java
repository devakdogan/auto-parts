package com.ape.dao;

import com.ape.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Long> {
    User findByEmail(String email);
}
