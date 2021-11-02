package com.omniteam.backofisbackend.dto.customer;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactAddDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerAddContactsDto {
    private Integer customerId;
    private List<CustomerContactAddDto> customerContactAddDtoList;
}
