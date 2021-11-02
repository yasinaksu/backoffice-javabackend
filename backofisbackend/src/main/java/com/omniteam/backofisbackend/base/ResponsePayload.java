package com.omniteam.backofisbackend.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponsePayload implements Serializable {

    private static final long serialVersionUID = 7675692754399303861L;

    private Integer statusCode;
    private String errorMessage;
    private String errorCode;
    private Object result;

    public ResponsePayload() {
        super();
    }

    public ResponsePayload(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public ResponsePayload(Integer statusCode, Object result) {
        this.statusCode = statusCode;
        this.result = result;
    }

    public ResponsePayload(Integer statusCode, String errorMessage, String errorCode) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }


}
