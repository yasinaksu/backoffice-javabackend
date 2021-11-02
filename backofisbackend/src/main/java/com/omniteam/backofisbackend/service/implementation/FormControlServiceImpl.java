package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.dto.formcontrol.FormControlDto;
import com.omniteam.backofisbackend.entity.FormControl;
import com.omniteam.backofisbackend.repository.FormControlRepository;
import com.omniteam.backofisbackend.service.FormControlService;
import com.omniteam.backofisbackend.shared.mapper.FormControlMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.SuccessDataResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormControlServiceImpl implements FormControlService {
    private final FormControlRepository formControlRepository;
    private final FormControlMapper formControlMapper;

    public DataResult<List<FormControlDto>> getFormControlsByFormId (Integer formId){
        List<FormControlDto> formControlDtos =this.formControlMapper.toFormControlDtoList(this.formControlRepository.getFormControlsByForm_FormIdOrderBySorterAsc(formId));
        return new SuccessDataResult<>(formControlDtos);
    }
}
