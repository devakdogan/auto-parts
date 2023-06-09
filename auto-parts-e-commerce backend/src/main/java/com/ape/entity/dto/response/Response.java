package com.ape.entity.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {
    private String message;
    private boolean success;


    public Response(String message) {
        this.message = message;
    }
    public Response(boolean success) {
        this.success = success;
    }

    public Response(String message, boolean success) {
        this.success = success;
        this.message = message;
    }
}
