package com.ape.business.abstracts;

import com.ape.dto.request.RegisterRequest;
import com.ape.dto.response.UserDTO;
import com.ape.entity.User;

public interface UserService {
    void createUser(RegisterRequest registerRequest);
    User getUserByEmail(String email);
    UserDTO confirmAccount(String token);
    void activateUser(String email);
    User dtoToEntity(UserDTO userDTO);
    UserDTO entityToDto(User user);
    UserDTO getUserById(Long id);
}
