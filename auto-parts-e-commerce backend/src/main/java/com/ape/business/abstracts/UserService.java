package com.ape.business.abstracts;

import com.ape.dto.request.RegisterRequest;
import com.ape.dto.response.UserDTO;
import com.ape.entity.UserEntity;

public interface UserService {
    void createUser(RegisterRequest registerRequest);
    UserEntity getUserByEmail(String email);
    UserDTO confirmAccount(String token);
    void activateUser(String email);
    UserDTO getUserById(Long id);
}
