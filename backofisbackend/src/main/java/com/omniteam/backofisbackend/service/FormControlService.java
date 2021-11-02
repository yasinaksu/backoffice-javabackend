package com.omniteam.backofisbackend.service;

import com.omniteam.backofisbackend.dto.formcontrol.FormControlDto;
import com.omniteam.backofisbackend.entity.FormControl;
import com.omniteam.backofisbackend.shared.result.DataResult;

import java.util.List;

public interface FormControlService {
    DataResult<List<FormControlDto>> getFormControlsByFormId (Integer formId);
}
