package com.omniteam.backofisbackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.omniteam.backofisbackend.shared.result.Result;

public interface AMQPService {
    Result sendSystemEmail(String to, String message) throws JsonProcessingException;
}
