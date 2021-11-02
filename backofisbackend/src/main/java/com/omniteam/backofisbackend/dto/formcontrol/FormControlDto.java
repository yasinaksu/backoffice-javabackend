package com.omniteam.backofisbackend.dto.formcontrol;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.omniteam.backofisbackend.entity.Form;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FormControlDto {
    private Integer formControlId;

    private String controlType;

    private String controlName;

    private String controlValues;

    private String controlValidators;

    private String placeHolder;

    private String hint;

    private String label;

    private String width;

    private String formClass;

    private String formName;
}
