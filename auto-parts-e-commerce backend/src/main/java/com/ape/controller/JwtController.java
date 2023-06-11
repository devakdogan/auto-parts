package com.ape.controller;

import com.ape.business.abstracts.UserService;
import com.ape.entity.dto.UserDTO;
import com.ape.entity.dto.request.LoginRequest;
import com.ape.entity.dto.request.RegisterRequest;
import com.ape.entity.dto.response.DataResponse;
import com.ape.entity.dto.response.LoginResponse;
import com.ape.entity.dto.response.Response;
import com.ape.entity.dto.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class JwtController {

    private final UserService userService;
    @Operation(summary = "Creating a new user record")
    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@Valid @RequestBody RegisterRequest registerRequest)  {
        userService.createUser(registerRequest);
        Response response = new Response(ResponseMessage.REGISTER_RESPONSE_MESSAGE,true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(path = "/confirm")
    @Operation(summary = "Confirm account with status pending")
    public ResponseEntity<DataResponse<UserDTO>> confirmAccount(@RequestParam String token){
        UserDTO userDTO = userService.confirmAccount(token);
        DataResponse<UserDTO> response = new DataResponse<>(ResponseMessage.ACCOUNT_CONFIRMED_RESPONSE,true,userDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user")
    public ResponseEntity<LoginResponse> authenticate(@RequestHeader(value = "cartUUID",required = false)String cartUUID, @Valid @RequestBody LoginRequest loginRequest)  {
        LoginResponse response = userService.loginUser(cartUUID,loginRequest);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
