package com.escolatecnica.api.root.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class APIException extends RuntimeException {
    private Integer status;

    public APIException(String message) {
        super(message);
        this.status = 500;
    }

    public APIException(String message, Integer status) {
        super(message);
        this.status = status;
    }

    public APIException(String message, Throwable cause) {
        super(message, cause);
        this.status = 500;
    }

}
