package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.dto.district.DistrictDto;
import com.omniteam.backofisbackend.entity.District;
import com.omniteam.backofisbackend.repository.DistrictRepository;
import com.omniteam.backofisbackend.shared.mapper.DistrictMapper;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DistrictServiceImplTest {
    @Mock
    private DistrictRepository districtRepository;

    @Mock
    private SecurityVerificationServiceImpl securityVerificationService;

    @Mock
    private  LogServiceImpl logService;

    @Spy
    private DistrictMapper districtMapper = Mappers.getMapper(DistrictMapper.class);

    @InjectMocks
    private DistrictServiceImpl districtService;
    @Test
    void getDistrictsByCity() {
        List<District> districtList = Arrays.asList(new District(),new District());

        Mockito.when(
                this.districtRepository.getDistrictsByCity(Mockito.anyInt())
        ).thenReturn(districtList);

        DataResult<List<DistrictDto>> dataResult = this.districtService.getDistrictsByCity(1);
        Assertions.assertThat(dataResult).isNotNull();
        Assertions.assertThat(dataResult.isSuccess()).isTrue();
        Assertions.assertThat(dataResult.getData()).hasSize(2);
    }
}