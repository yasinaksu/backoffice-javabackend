package com.omniteam.backofisbackend.shared.mapper;

import com.omniteam.backofisbackend.dto.order.OrderDetailDto;
import com.omniteam.backofisbackend.entity.OrderDetail;
import com.omniteam.backofisbackend.requests.order.OrderDetailAddRequest;
import com.omniteam.backofisbackend.requests.order.OrderDetailUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(
    componentModel = "spring"
)
public interface OrderDetailMapper {
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
    void update(@MappingTarget OrderDetail orderDetail, OrderDetailUpdateRequest orderDetailUpdateRequest);


}
