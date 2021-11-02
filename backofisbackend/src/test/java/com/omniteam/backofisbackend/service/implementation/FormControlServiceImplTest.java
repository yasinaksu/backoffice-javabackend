package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.dto.formcontrol.FormControlDto;
import com.omniteam.backofisbackend.entity.FormControl;
import com.omniteam.backofisbackend.repository.FormControlRepository;
import com.omniteam.backofisbackend.service.FormControlService;
import com.omniteam.backofisbackend.shared.mapper.FormControlMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FormControlServiceImplTest {
    @Mock
    private FormControlRepository formControlRepository;
    @Spy
    private FormControlMapper formControlMapper = Mappers.getMapper(FormControlMapper.class);
    private FormControlService formControlService;

    @BeforeEach
    void setUp() {
        formControlService = new FormControlServiceImpl(
                formControlRepository,
                formControlMapper
        );
    }

    @Test
    void getFormControlsByFormId() {
        List<FormControl> formControlList = Arrays.asList(new FormControl(),new FormControl());
        Mockito.when(
                this.formControlRepository.getFormControlsByForm_FormIdOrderBySorterAsc(Mockito.anyInt())
        ).thenReturn(formControlList);

        DataResult<List<FormControlDto>> dataResult = this.formControlService.getFormControlsByFormId(1);

        Assertions.assertThat(dataResult).isNotNull();
        Assertions.assertThat(dataResult.isSuccess()).isTrue();
        Assertions.assertThat(dataResult.getData()).hasSize(2);
    }
}