package com.omniteam.backofisbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.omniteam.backofisbackend.jms.EmailPublisherMQ;
import com.omniteam.backofisbackend.service.AMQPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mq")
public class MQController {



    @Autowired
    AMQPService amqpService;

    @GetMapping
    public ResponseEntity runMQTest() throws JsonProcessingException {
        amqpService.sendSystemEmail("orcunozbay@gmail.com","test mail");
        return ResponseEntity.ok().build();
    }


}
