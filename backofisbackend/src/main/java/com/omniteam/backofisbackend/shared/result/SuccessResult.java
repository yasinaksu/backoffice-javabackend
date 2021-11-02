package com.omniteam.backofisbackend.shared.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omniteam.backofisbackend.entity.BaseEntity;
import com.omniteam.backofisbackend.entity.JobRequest;

public class SuccessResult extends Result {

    public SuccessResult(){
        super(true);
    }

    public SuccessResult(BaseEntity baseEntity) throws JsonProcessingException {
        super(true,objectMapper().writeValueAsString(baseEntity));
    }

    public SuccessResult(Integer id,BaseEntity baseEntity) throws JsonProcessingException {
        super(true,id,objectMapper().writeValueAsString(baseEntity));
    }

    public SuccessResult(String message){
        super(true,message);
    }


    public SuccessResult(Integer id) {
        super(true,id);
    }
    public SuccessResult(Integer id,String message) {
        super(true,id);
        this.setMessage(message);
    }
}
