package com.ape.controller;

import com.ape.business.concretes.UserManager;
import com.ape.dto.request.RegisterRequest;
import com.ape.dto.response.UserDTO;
import com.ape.utility.DataResponse;
import com.ape.utility.Response;
import com.ape.utility.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtController {

    private final UserManager userManager;
    @Operation(summary = "Creating a new User record")
    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@Valid @RequestBody RegisterRequest registerRequest)  {
        userManager.createUser(registerRequest);
        Response response = new Response(true,ResponseMessage.REGISTER_RESPONSE_MESSAGE);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
