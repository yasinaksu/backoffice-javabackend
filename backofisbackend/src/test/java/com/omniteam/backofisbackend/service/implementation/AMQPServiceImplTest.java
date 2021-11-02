package com.omniteam.backofisbackend.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.omniteam.backofisbackend.jms.EmailMessage;
import com.omniteam.backofisbackend.jms.EmailPublisherMQ;
import com.omniteam.backofisbackend.shared.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class AMQPServiceImplTest {

    @InjectMocks
    AMQPServiceImpl amqpService;

    @Mock
    EmailPublisherMQ publisherMQ;


    @Test
    void sendSystemEmailTest() throws JsonProcessingException {
        EmailMessage emailMessageMocked=new EmailMessage();
        Mockito.when(publisherMQ.send(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(emailMessageMocked);

        Result result=amqpService.sendSystemEmail("tester","message");

        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isNotNull();
    }

    @Test
    void sendSystemEmailFailTest() throws JsonProcessingException {
        EmailMessage emailMessageMocked=new EmailMessage();
        Mockito.when(publisherMQ.send(Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(emailMessageMocked);

        assertThatThrownBy(()->amqpService.sendSystemEmail(null,null));
    }


}
