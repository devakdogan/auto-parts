package com.ape.business.abstracts;

import com.ape.dto.UserDTO;
import com.ape.dto.request.LoginRequest;
import com.ape.dto.request.RegisterRequest;
import com.ape.dto.request.UserUpdateRequest;
import com.ape.dto.response.LoginResponse;
import com.ape.entity.UserEntity;
import com.ape.entity.enums.RoleType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    PageImpl<UserDTO> getAllUsersWithFilterAndPage(String query, RoleType role, Pageable pageable);
    void createUser(RegisterRequest registerRequest);
    UserEntity getUserByEmail(String email);
    UserDTO confirmAccount(String token);
    UserDTO getUserById(Long id);
    UserDTO getPrincipal();
    UserEntity getCurrentUser();
    LoginResponse loginUser(String cartUUID, LoginRequest loginRequest);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(UserUpdateRequest userUpdateRequest);
    void resetFailedAttempts(UserEntity user);
    void increaseFailedAttempts(UserEntity user);
    void lock(UserEntity user);


}
