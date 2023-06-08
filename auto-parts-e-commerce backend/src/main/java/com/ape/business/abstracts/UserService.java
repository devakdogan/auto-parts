package com.ape.business.abstracts;

import com.ape.dto.UserDTO;
import com.ape.dto.request.LoginRequest;
import com.ape.dto.request.RegisterRequest;
import com.ape.dto.response.LoginResponse;
import com.ape.entity.UserEntity;

import java.util.List;

public interface UserService {
    void createUser(RegisterRequest registerRequest);
    UserEntity getUserByEmail(String email);
    UserDTO confirmAccount(String token);
    UserDTO getUserById(Long id);
    LoginResponse loginUser(String cartUUID, LoginRequest loginRequest);
    List<UserDTO> getAllUsers();
}
