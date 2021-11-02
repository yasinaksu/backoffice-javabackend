package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.entity.Log;
import com.omniteam.backofisbackend.entity.User;
import com.omniteam.backofisbackend.enums.EnumLogIslemTipi;
import com.omniteam.backofisbackend.repository.LogRepository;
import com.omniteam.backofisbackend.repository.UserRepository;
import com.omniteam.backofisbackend.shared.constant.ResultMessage;
 import com.omniteam.backofisbackend.shared.result.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
 import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
 public class LogServiceImplTest {

    @InjectMocks
    private LogServiceImpl logService;

    @Mock
    private LogRepository logRepository;

    @Mock
    private UserRepository userRepository;


    @Test
   public void loglama() {
        User user =new User();
        user.setUserId(5);
        lenient().when(userRepository.getById(Mockito.any())).thenReturn(user);

        Log  log =new Log();
        log.setUser(user);
        log.setLogData(EnumLogIslemTipi.CustomerAddContacts.getValue());
        log.setCreatedDate(LocalDateTime.now());

        Mockito.when(logRepository.save(Mockito.any())).thenReturn(log);

        Result serviceResult = logService.loglama(EnumLogIslemTipi.CustomerAddContacts,user);

        Assertions.assertEquals(serviceResult.getMessage(), ResultMessage.LOG_SAVE);
    }
}
