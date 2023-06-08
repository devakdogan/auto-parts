package com.ape.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataResponse<T> extends Response {
    private T data;

    public DataResponse(String message, boolean success,T data) {
        super(message, success);
        this.data = data;
    }

    public DataResponse(boolean success,T data) {
        super(success);
        this.data = data;
    }
}
