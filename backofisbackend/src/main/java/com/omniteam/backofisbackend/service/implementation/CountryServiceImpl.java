package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.base.annotions.LogMethodCall;
import com.omniteam.backofisbackend.dto.country.CountryDto;
import com.omniteam.backofisbackend.entity.Country;
import com.omniteam.backofisbackend.enums.EnumLogIslemTipi;
import com.omniteam.backofisbackend.repository.CountryRepository;
import com.omniteam.backofisbackend.service.CountryService;
import com.omniteam.backofisbackend.shared.mapper.CountryMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.SuccessDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {
    @Autowired
    private SecurityVerificationServiceImpl securityVerificationService;

    @Autowired
    private  LogServiceImpl logService;

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CountryMapper countryMapper;

    @LogMethodCall(value = "Country getAll is started")
    @Override
    public DataResult<List<CountryDto>> getAll(String countryName) {
        List<Country> countryList = this.countryRepository.findFirst10ByCountryNameContainingIgnoreCaseOrderByCountryIdAsc(countryName);
        List<CountryDto> countryDtoList = this.countryMapper.toCountryDtoList(countryList);
        logService.loglama(EnumLogIslemTipi.GetCountries,securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {}
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall =  m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult<>("",countryDtoList);
    }
}
