package com.omniteam.backofisbackend.dto.customercontact;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerContactDto {
    private Integer customerContactId;
    private Integer customerId;
    private Integer countryId;
    private Integer cityId;
    private Integer districtId;
    private String countryName;
    private String cityName;
    private String districtName;
    private String contactType;
    private String contactValue;
    private String contactDescription;
    private Boolean isActive;
}
