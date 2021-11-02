package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.dto.country.CountryDto;
import com.omniteam.backofisbackend.entity.Country;
import com.omniteam.backofisbackend.repository.CountryRepository;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CountryServiceImplTest {
    @Mock
    private SecurityVerificationServiceImpl securityVerificationService;

    @Mock
    private  LogServiceImpl logService;

    @Mock
    private CountryRepository countryRepository;

    @Spy
    private CountryMapper countryMapper = Mappers.getMapper(CountryMapper.class);

    @InjectMocks
    private CountryServiceImpl countryService;
    @Test
    public void getAll() {
        List<Country> countryList = Arrays.asList(new Country(),new Country());
        Mockito.when(
                this.countryRepository
                        .findFirst10ByCountryNameContainingIgnoreCaseOrderByCountryIdAsc(Mockito.anyString())
        ).thenReturn(countryList);

        DataResult<List<CountryDto>> dataResult = this.countryService.getAll("test");

        Assertions.assertThat(dataResult).isNotNull();
        Assertions.assertThat(dataResult.isSuccess()).isTrue();
        Assertions.assertThat(dataResult.getData()).hasSize(2);

    }
}