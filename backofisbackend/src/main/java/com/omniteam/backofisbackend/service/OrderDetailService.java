package com.omniteam.backofisbackend.service;

import com.omniteam.backofisbackend.dto.order.OrderDetailDto;
import com.omniteam.backofisbackend.requests.order.RemoveProductFromCartRequest;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.Result;

import java.util.List;

public interface OrderDetailService {
    DataResult<List<OrderDetailDto>> getByOrderId(int orderId);
    Result deleteOrderDetail(RemoveProductFromCartRequest removeProductFromCartRequest);
}
