package com.ape.utility;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Response {
    private boolean success;
    private String message;

    public Response(boolean success) {
        this.success = success;
    }

    public Response(boolean success,String message) {
        this(success);
        this.message = message;
    }
}
