package com.ape.entity.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends Response {

    private String token;

    private String cartUUID;

    public LoginResponse(String message, String token, String cartUUID) {
        super(message);
        this.token = token;
        this.cartUUID = cartUUID;
    }

    public LoginResponse(String message,boolean success, String token, String cartUUID) {
        super(message,success);
        this.token = token;
        this.cartUUID = cartUUID;
    }
}
