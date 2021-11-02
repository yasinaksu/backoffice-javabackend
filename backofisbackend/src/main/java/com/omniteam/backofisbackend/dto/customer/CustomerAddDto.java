package com.omniteam.backofisbackend.dto.customer;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactAddDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerAddDto {
    private String firstName;
    private String lastName;
    private String nationNumber;
    private List<CustomerContactAddDto> customerContactList;
}
