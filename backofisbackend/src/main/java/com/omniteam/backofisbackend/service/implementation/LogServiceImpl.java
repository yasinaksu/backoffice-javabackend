package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.entity.Log;
import com.omniteam.backofisbackend.entity.User;
import com.omniteam.backofisbackend.enums.EnumLogIslemTipi;
import com.omniteam.backofisbackend.repository.LogRepository;
import com.omniteam.backofisbackend.repository.UserRepository;
import com.omniteam.backofisbackend.service.LogService;
import com.omniteam.backofisbackend.shared.constant.ResultMessage;
import com.omniteam.backofisbackend.shared.result.Result;
import com.omniteam.backofisbackend.shared.result.SuccessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service("logService")
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Transactional
    public Result loglama(EnumLogIslemTipi islemTip, User user){
        Log log=new Log();
     //   User user =userRepository.getById(userId);
        log.setUser(user);
        log.setLogData(islemTip.getValue());
        log.setCreatedDate(LocalDateTime.now());

        logRepository.save(log);

        return new SuccessResult(ResultMessage.LOG_SAVE);

    }
}
