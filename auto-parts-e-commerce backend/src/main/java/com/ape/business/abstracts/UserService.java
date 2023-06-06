package com.ape.business.abstracts;

import com.ape.dto.request.RegisterRequest;
import com.ape.dto.response.UserDTO;
import com.ape.entity.User;
import com.ape.utility.DataResponse;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface UserService {
    void createUser(RegisterRequest registerRequest);
    User getUserByEmail(String email);



}
