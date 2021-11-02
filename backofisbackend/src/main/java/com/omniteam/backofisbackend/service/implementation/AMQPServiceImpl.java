package com.omniteam.backofisbackend.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.omniteam.backofisbackend.jms.EmailPublisherMQ;
import com.omniteam.backofisbackend.service.AMQPService;
import com.omniteam.backofisbackend.shared.result.ErrorResult;
import com.omniteam.backofisbackend.shared.result.Result;
import com.omniteam.backofisbackend.shared.result.SuccessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AMQPServiceImpl implements AMQPService {

    @Autowired
    EmailPublisherMQ publisherMQ;


    @Override
    public Result sendSystemEmail(String to, String message) throws JsonProcessingException {
            publisherMQ.send("system@test.com",to,message);
            return new SuccessResult("The message has been sent");
    }
}


