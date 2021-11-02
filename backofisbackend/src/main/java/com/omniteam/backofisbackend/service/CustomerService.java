package com.omniteam.backofisbackend.service;

import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.customer.CustomerAddDto;
import com.omniteam.backofisbackend.dto.customer.CustomerDto;
import com.omniteam.backofisbackend.dto.customer.CustomerGetAllDto;
import com.omniteam.backofisbackend.dto.customer.CustomerUpdateDto;
import com.omniteam.backofisbackend.dto.order.OrderDto;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.Result;

public interface CustomerService {
    DataResult<PagedDataWrapper<CustomerGetAllDto>> getAll(int page, int size, String searchKey) throws InterruptedException;
    Result add(CustomerAddDto customerAddDto);
    Result update(CustomerUpdateDto customerUpdateDto);

    DataResult<CustomerDto> getById(int customerId);

    DataResult<OrderDto> getOrderByCustomerIdAndStatus(Integer customerId, String status);

    public void clearCustomerGetAllCache();
}
