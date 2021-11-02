package com.omniteam.backofisbackend.dto.customer;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerDto {
    private Integer customerId;
    private String nationNumber;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    List<CustomerContactDto> customerContactDtoList;
}
