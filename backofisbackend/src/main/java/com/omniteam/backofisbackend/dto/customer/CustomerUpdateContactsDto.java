package com.omniteam.backofisbackend.dto.customer;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactAddDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerUpdateContactsDto {
    private List<CustomerContactUpdateDto> customerContactUpdateDtoList;
}
