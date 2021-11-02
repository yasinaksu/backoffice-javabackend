package com.omniteam.backofisbackend.dto.customercontact;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerContactAddDto {
    private Integer countryId;
    private Integer cityId;
    private Integer districtId;
    private Boolean isDefault;
    private String contactType;
    private String contactValue;
    private String contactDescription;
}
