package com.omniteam.backofisbackend.service;

import com.omniteam.backofisbackend.entity.User;
import com.omniteam.backofisbackend.enums.EnumLogIslemTipi;
import com.omniteam.backofisbackend.shared.result.Result;

public interface LogService {

    public Result loglama(EnumLogIslemTipi islemTip, User user);
}
