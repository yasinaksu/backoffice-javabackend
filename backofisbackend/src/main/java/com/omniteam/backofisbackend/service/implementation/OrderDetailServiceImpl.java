package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.base.annotions.LogMethodCall;
import com.omniteam.backofisbackend.dto.order.OrderDetailDto;
import com.omniteam.backofisbackend.entity.Order;
import com.omniteam.backofisbackend.entity.OrderDetail;
import com.omniteam.backofisbackend.enums.EnumLogIslemTipi;
import com.omniteam.backofisbackend.repository.OrderDetailRepository;
import com.omniteam.backofisbackend.repository.OrderRepository;
import com.omniteam.backofisbackend.requests.order.RemoveProductFromCartRequest;
import com.omniteam.backofisbackend.service.OrderDetailService;
import com.omniteam.backofisbackend.shared.mapper.OrderDetailMapper;
import com.omniteam.backofisbackend.shared.result.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private SecurityVerificationServiceImpl securityVerificationService;
    @Autowired
    private LogServiceImpl logService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailMapper orderDetailMapper;


    @LogMethodCall(value = "getByOrderId is started")
    @Override
    public DataResult<List<OrderDetailDto>> getByOrderId(int orderId) {
        if (orderId == 0) {
            return new ErrorDataResult<>("Lütfen detaylarını görmek istediğiniz siparişi belirtiniz", null);
        }
        Order order = this.orderRepository.getById(orderId);
        List<OrderDetail> orderDetails = this.orderDetailRepository.getOrderDetailsByOrder(order);
        List<OrderDetailDto> orderDetailDtoList = this.orderDetailMapper.toOrderDetailDtoList(orderDetails);
        logService.loglama(EnumLogIslemTipi.GetOrderDetails, securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {
        }
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult<>(orderDetailDtoList);
    }


    @Override
    @Transactional
    public Result deleteOrderDetail(RemoveProductFromCartRequest removeProductFromCartRequest) {
        this.orderDetailRepository.deleteOrderDetailByOrderDetailId(removeProductFromCartRequest.getOrderDetailId());
        return new SuccessResult(0, "Sepetten Silme Başarılı");
    }
}
