package com.omniteam.backofisbackend.controller;

import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.order.AddProductToCartRequest;
import com.omniteam.backofisbackend.dto.order.OrderDetailDto;
import com.omniteam.backofisbackend.dto.order.OrderDto;
import com.omniteam.backofisbackend.requests.order.*;
import com.omniteam.backofisbackend.security.jwt.JwtTokenUtil;
import com.omniteam.backofisbackend.service.OrderDetailService;
import com.omniteam.backofisbackend.service.OrderService;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.Result;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/carts")
public class OrdersController {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

     @Autowired
    JwtTokenUtil tokenUtil;

    @Autowired
    public OrdersController(OrderService orderService, OrderDetailService orderDetailService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
    }

    @GetMapping(
            path = "/getbyid/{orderid}"
    )
    public ResponseEntity<DataResult<OrderDto>> getById(@PathVariable(name = "orderid") int orderId) throws Exception {
        return ResponseEntity.ok(this.orderService.getById(orderId));
    }

    @PostMapping(
            path = "/getall"
    )
    public ResponseEntity<DataResult<PagedDataWrapper<OrderDto>>> getAll(@RequestBody OrderGetAllRequest orderGetAllRequest) {
        return ResponseEntity.ok(this.orderService.getAll(orderGetAllRequest));
    }

    @PostMapping("/export/report")
    public ResponseEntity<?> exportFullReport(HttpServletRequest httpServletRequest) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ","");
        String loggedUsername = tokenUtil.getUsernameFromToken(token);
        this.orderService.startOrderReportExport(loggedUsername);
        return ResponseEntity.ok().build();
    }


    @GetMapping(
            path = "/getorderdetails/{orderid}"
    )
    public ResponseEntity<DataResult<List<OrderDetailDto>>> getOrderDetails(@PathVariable(name = "orderid") int orderId) {
        return ResponseEntity.ok(this.orderDetailService.getByOrderId(orderId));
    }

    @PostMapping(
            path = "/add"
    )
    public ResponseEntity<DataResult<OrderDto>> add(@RequestBody OrderAddRequest orderAddRequest) {
        return ResponseEntity.ok(this.orderService.add(orderAddRequest));
    }

    @PostMapping(
            path = "/update"
    )
    public ResponseEntity<DataResult<OrderDto>> update(@RequestBody OrderUpdateRequest orderUpdateRequest) {
        return ResponseEntity.ok(this.orderService.update(orderUpdateRequest));
    }

    @PostMapping(
            path = "/delete"
    )
    public ResponseEntity<Result> delete(@RequestBody OrderDeleteRequest orderDeleteRequest) {
        return ResponseEntity.ok(this.orderService.delete(orderDeleteRequest));
    }

    @PostMapping(
            path = "/addProductToCart"
    )
    public ResponseEntity<Result> addProductToCart(@RequestBody AddProductToCartRequest addProductToCartRequest) throws Exception {
        return ResponseEntity.ok(this.orderService.addProductToCart(addProductToCartRequest));
    }

    @PostMapping(
            path = "/removeProductFromCart"
    )
    public ResponseEntity<Result> addProductToCart(@RequestBody RemoveProductFromCartRequest removeProductFromCartRequest) {
        return ResponseEntity.ok(this.orderDetailService.deleteOrderDetail(removeProductFromCartRequest));
    }
    //TODO sepete ürün ekleyen endpoint
    //TODO sepetteki ürünü güncelleme ve silme
}
