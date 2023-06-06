package com.ape.business.abstracts;

import com.ape.dto.request.RegisterRequest;
import com.ape.entity.User;

public interface UserService {
    void createUser(RegisterRequest registerRequest);
    User getUserByEmail(String email);



}
