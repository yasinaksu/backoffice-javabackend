package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.base.annotions.LogMethodCall;
import com.omniteam.backofisbackend.dto.district.DistrictDto;
import com.omniteam.backofisbackend.entity.District;
import com.omniteam.backofisbackend.enums.EnumLogIslemTipi;
import com.omniteam.backofisbackend.repository.DistrictRepository;
import com.omniteam.backofisbackend.service.DistrictService;
import com.omniteam.backofisbackend.shared.mapper.DistrictMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.SuccessDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {
    @Autowired
    private SecurityVerificationServiceImpl securityVerificationService;

    @Autowired
    private  LogServiceImpl logService;

    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private DistrictMapper districtMapper;

    @LogMethodCall(value = "getDistrictsByCity is started")
    @Override
    public DataResult<List<DistrictDto>> getDistrictsByCity(int cityId) {
        List<District> districtList = this.districtRepository.getDistrictsByCity(cityId);
        List<DistrictDto> districtDtoList = this.districtMapper.toDistrictDtoList(districtList);
        logService.loglama(EnumLogIslemTipi.GetDistrictByCity,securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {}
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall =  m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult<>(districtDtoList);
    }
}
