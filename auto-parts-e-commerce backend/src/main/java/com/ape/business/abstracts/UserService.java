package com.ape.business.abstracts;

import com.ape.entity.dto.UserDTO;
import com.ape.entity.dto.UserDeleteDTO;
import com.ape.entity.dto.request.LoginRequest;
import com.ape.entity.dto.request.PasswordUpdateRequest;
import com.ape.entity.dto.request.RegisterRequest;
import com.ape.entity.dto.request.UserUpdateRequest;
import com.ape.entity.dto.response.LoginResponse;
import com.ape.entity.concrete.UserEntity;
import com.ape.entity.enums.RoleType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    PageImpl<UserDTO> getAllUsersWithFilterAndPage(String query, RoleType role, Pageable pageable);
    void createUser(RegisterRequest registerRequest);
    UserEntity getUserByEmail(String email);
    UserDTO confirmAccount(String token);
    UserDTO getUserDTOById(Long id);
    UserDTO getPrincipal();
    UserEntity getCurrentUser();
    LoginResponse loginUser(String cartUUID, LoginRequest loginRequest);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(UserUpdateRequest userUpdateRequest);
    long countUserRecords();
    UserEntity getUserById(Long userId);
    UserDeleteDTO adminRemoveUserById(Long id);
    List<UserEntity> findUserByRole(RoleType role);
    void updatePassword(PasswordUpdateRequest passwordUpdateRequest);
}
