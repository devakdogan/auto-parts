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
import org.springframework.web.bind.annotation.*;

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

    @PutMapping(path = "/confirm")
    public ResponseEntity<DataResponse<UserDTO>> confirmAccount(@RequestParam String token){
        UserDTO userDTO = userManager.confirmAccount(token);
        DataResponse<UserDTO> response = new DataResponse<UserDTO>(userDTO,true,ResponseMessage.ACCOUNT_CONFIRMED_RESPONSE);
        return ResponseEntity.ok(response);
    }
}
