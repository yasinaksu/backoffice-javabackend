package com.omniteam.backofisbackend.service;

import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.order.AddProductToCartRequest;
import com.omniteam.backofisbackend.dto.order.OrderDto;
import com.omniteam.backofisbackend.requests.order.OrderAddRequest;
import com.omniteam.backofisbackend.requests.order.OrderDeleteRequest;
import com.omniteam.backofisbackend.requests.order.OrderGetAllRequest;
import com.omniteam.backofisbackend.requests.order.OrderUpdateRequest;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.Result;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

public interface OrderService {
    DataResult<OrderDto> getById(int orderId) throws Exception;

    DataResult<PagedDataWrapper<OrderDto>> getAll(OrderGetAllRequest orderGetAllRequest);

    DataResult<?> startOrderReportExport(String username) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException;


    DataResult<OrderDto> add(OrderAddRequest orderAddRequest);

    DataResult<OrderDto> update(OrderUpdateRequest orderUpdateRequest);

    Result delete(OrderDeleteRequest orderDeleteRequest);

    DataResult<OrderDto> addProductToCart(AddProductToCartRequest addProductToCartRequest) throws Exception;
}
