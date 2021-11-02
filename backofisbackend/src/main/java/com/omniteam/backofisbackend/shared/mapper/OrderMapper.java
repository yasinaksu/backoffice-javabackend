package com.omniteam.backofisbackend.shared.mapper;

import com.omniteam.backofisbackend.dto.customer.CustomerDto;
import com.omniteam.backofisbackend.dto.customer.CustomerUpdateDto;
import com.omniteam.backofisbackend.dto.order.AddProductToCartRequest;
import com.omniteam.backofisbackend.dto.order.OrderDetailDto;
import com.omniteam.backofisbackend.dto.order.OrderDto;
import com.omniteam.backofisbackend.entity.Customer;
import com.omniteam.backofisbackend.entity.Order;
import com.omniteam.backofisbackend.entity.OrderDetail;
import com.omniteam.backofisbackend.requests.order.OrderAddRequest;
import com.omniteam.backofisbackend.requests.order.OrderDetailAddRequest;
import com.omniteam.backofisbackend.requests.order.OrderUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface OrderMapper {

    @Mapping(target = "createdDate", expression = "java(order.getCreatedDate())")
    OrderDto toOrderDto(Order order);

    List<OrderDto> toOrderDtoList(List<Order> orders);

    @Mapping(target = "user.userId", source = "userId")
    @Mapping(target = "customer.customerId", source = "customerId")
    Order toOrderFromOrderAddRequest(OrderAddRequest orderAddRequest);

    @Mapping(target = "orderDetails", ignore = true)
    void update(@MappingTarget Order order, OrderUpdateRequest orderUpdateRequest);

    @Mapping(target = "customerContactDtoList",source = "customerContacts")
    CustomerDto toCustomerDto(Customer customer);


    @Mapping(target = "productName",source = "product.productName")
    @Mapping(target = "productDescription",source = "product.description")
    @Mapping(target = "productShortDescription",source = "product.shortDescription")
    @Mapping(target = "productBarcode",source = "product.barcode")
    @Mapping(target = "productPrice",source = "productPrice.actualPrice")
    @Mapping(target = "productId",source = "product.productId")
    @Mapping(target = "orderId",source = "order.orderId")
    OrderDetailDto toOrderDetailDto(OrderDetail orderDetail);
    List<OrderDetailDto> toOrderDetailDtoList(List<OrderDetail> orderDetailList);

    @Mapping(target = "product.productId", source = "productId")
    OrderDetail toOrderDetailFromOrderDetailAddRequest(OrderDetailAddRequest orderDetailAddRequest);
    List<OrderDetail> toOrderDetailFromOrderDetailAddRequestList(List<OrderDetailAddRequest> orderDetailAddRequestList);
}
