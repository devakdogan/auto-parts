package com.ape.utility;

import lombok.Getter;

@Getter
public class DataResponse<T> extends Response {
    private T data;

    public DataResponse(T data, boolean success, String message) {
        super(success, message);
        this.data = data;
    }

    public DataResponse(T data, boolean success) {
        super(success);
        this.data = data;
    }
}
