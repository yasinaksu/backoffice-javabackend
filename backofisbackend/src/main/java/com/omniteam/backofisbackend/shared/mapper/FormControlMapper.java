package com.omniteam.backofisbackend.shared.mapper;

import com.omniteam.backofisbackend.dto.formcontrol.FormControlDto;
import com.omniteam.backofisbackend.entity.FormControl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface FormControlMapper {

    List<FormControlDto> toFormControlDtoList(List<FormControl> formControlList);

    @Mapping(target = "formName",source = "form.formName")
    @Mapping(target = "formClass",source = "form.formClass")
    FormControlDto toFormControlDto(FormControl formControl);
}
