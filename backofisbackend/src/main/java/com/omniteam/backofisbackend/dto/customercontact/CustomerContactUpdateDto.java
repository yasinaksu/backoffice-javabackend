package com.omniteam.backofisbackend.dto.customercontact;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerContactUpdateDto {
    private Integer customerContactId;
    private Integer countryId;
    private Integer cityId;
    private Integer districtId;
    private Boolean isDefault;
    private String contactType;
    private String contactValue;
    private String contactDescription;
    private Boolean isActive;
}
