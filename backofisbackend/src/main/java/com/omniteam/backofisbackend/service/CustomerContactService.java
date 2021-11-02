package com.omniteam.backofisbackend.service;

import com.omniteam.backofisbackend.dto.customer.CustomerAddContactsDto;
import com.omniteam.backofisbackend.dto.customer.CustomerUpdateContactsDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactAddDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactUpdateDto;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.Result;

import java.util.List;

public interface CustomerContactService {

    DataResult<List<CustomerContactDto>> getByCustomerId(int customerId);

    Result add(CustomerAddContactsDto customerAddContactsDto);

    Result update(CustomerUpdateContactsDto customerUpdateContactsDto);
}
