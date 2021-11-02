package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.base.annotions.LogMethodCall;
import com.omniteam.backofisbackend.dto.city.CityDto;
import com.omniteam.backofisbackend.entity.City;
import com.omniteam.backofisbackend.enums.EnumLogIslemTipi;
import com.omniteam.backofisbackend.repository.CityRepository;
import com.omniteam.backofisbackend.service.CityService;
import com.omniteam.backofisbackend.shared.mapper.CityMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.SuccessDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private SecurityVerificationServiceImpl securityVerificationService;
    @Autowired
    private  LogServiceImpl logService;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CityMapper cityMapper;

    @LogMethodCall(value = "getCitiesByCountry is started")
    @Override
    public DataResult<List<CityDto>> getCitiesByCountry(int countryId) {
        List<City> cityList = this.cityRepository.getCitiesByCountryId(countryId);
        List<CityDto> cityDtoList = this.cityMapper.toCityDtoList(cityList);
        logService.loglama(EnumLogIslemTipi.GetCitiesByCountry,securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {}
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall =  m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult<>(cityDtoList);
    }
}
