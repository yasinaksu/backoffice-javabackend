package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.dto.city.CityDto;
import com.omniteam.backofisbackend.entity.City;
import com.omniteam.backofisbackend.repository.CityRepository;
import com.omniteam.backofisbackend.shared.mapper.CityMapper;
import com.omniteam.backofisbackend.shared.mapper.CountryMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CityServiceImplTest {

    @Mock
    private LogServiceImpl logService;
    @Mock
    private SecurityVerificationServiceImpl securityVerificationService;
    @Mock
    private CityRepository cityRepository;
    @Spy
    private CityMapper cityMapper = Mappers.getMapper(CityMapper.class);

    @InjectMocks
    private CityServiceImpl cityService;

    @Test
    public void getCitiesByCountry() {
        List<City> cityList = Arrays.asList(new City(),new City(),new City());
        Mockito.when(
                this.cityRepository.getCitiesByCountryId(Mockito.anyInt())
        ).thenReturn(cityList);

        DataResult<List<CityDto>> dataResult = this.cityService.getCitiesByCountry(1);

        Assertions.assertThat(dataResult).isNotNull();
        Assertions.assertThat(dataResult.isSuccess()).isTrue();
        Assertions.assertThat(dataResult.getData()).hasSize(3);
    }
}