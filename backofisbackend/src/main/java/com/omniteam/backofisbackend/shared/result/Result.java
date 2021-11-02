package com.omniteam.backofisbackend.shared.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Result {
    private boolean success;
    @Getter
    protected Integer id;
    private String message;

    public Result(boolean success) {
        this.success = success;
    }

    public Result(boolean success, String message) {
        this(success);
        this.message = message;
    }

    public Result(Boolean success, Integer id) {
        this.id = id;
        this.success = success;
    }

    public Result(Boolean success, Integer id,String message) {
        this.id = id;
        this.success = success;
        this.message=message;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getMessage() {
        return this.message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

    protected static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }


}
