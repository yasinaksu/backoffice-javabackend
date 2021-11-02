package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.base.annotions.LogMethodCall;
import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.order.AddProductToCartRequest;
import com.omniteam.backofisbackend.dto.order.OrderDto;
import com.omniteam.backofisbackend.entity.*;
import com.omniteam.backofisbackend.enums.EnumLogIslemTipi;
import com.omniteam.backofisbackend.repository.*;
import com.omniteam.backofisbackend.repository.customspecification.OrderSpec;
import com.omniteam.backofisbackend.requests.order.*;
import com.omniteam.backofisbackend.service.OrderService;
import com.omniteam.backofisbackend.service.SecurityVerificationService;
import com.omniteam.backofisbackend.shared.mapper.OrderDetailMapper;
import com.omniteam.backofisbackend.shared.mapper.OrderMapper;
import com.omniteam.backofisbackend.shared.result.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    Job orderExporterJob;

    @Autowired
    private LogServiceImpl logService;

    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ProductPriceRepository productPriceRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SecurityVerificationService securityVerificationService;

    @LogMethodCall(value = "OrderGetById is started")
    @Override
    public DataResult<OrderDto> getById(int orderId) throws Exception {
        Optional<Order> optionalOrder = this.orderRepository.findById(orderId);
        if (!optionalOrder.isPresent())
            throw new Exception("Order bulunamadı!");
        OrderDto orderDto = this.orderMapper.toOrderDto(optionalOrder.orElse(null));
        logService.loglama(EnumLogIslemTipi.OrdersGetById, securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {
        }
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult<>(orderDto.getOrderId(), orderDto);
    }

    @LogMethodCall(value = "OrderGetAll is started")
    @Override
    public DataResult<PagedDataWrapper<OrderDto>> getAll(OrderGetAllRequest orderGetAllRequest) {
        Pageable pageable = PageRequest.of(orderGetAllRequest.getPage(), orderGetAllRequest.getSize());
        Page<Order> orderPage =
                this.orderRepository.findAll(
                        OrderSpec.getAllByFilter(
                                orderGetAllRequest.getCustomerId(),
                                orderGetAllRequest.getStatus(),
                                orderGetAllRequest.getStartDate(),
                                orderGetAllRequest.getEndDate()
                        ), pageable);

        List<OrderDto> orderDtoList = this.orderMapper.toOrderDtoList(orderPage.getContent());
        PagedDataWrapper<OrderDto> pagedDataWrapper = new PagedDataWrapper<>(
                orderDtoList,
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.isLast()
        );
        logService.loglama(EnumLogIslemTipi.OrdersGetAll, securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {
        }
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult<>(pagedDataWrapper);
    }

    @Override
    public DataResult<?> startOrderReportExport(String username) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters parameters = new JobParametersBuilder()
                .addParameter("proccess-key", new JobParameter(UUID.randomUUID().toString()))
                .addParameter("user-email", new JobParameter(username))
                .toJobParameters();
        this.jobLauncher.run(orderExporterJob, parameters);
        return DataResult.builder().build();
    }

    @LogMethodCall(value = "OrderAdd is started")
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public DataResult<OrderDto> add(OrderAddRequest orderAddRequest) {
        Order order = this.orderMapper.toOrderFromOrderAddRequest(orderAddRequest);

        for (OrderDetail orderDetail : order.getOrderDetails()) {
            ProductPrice productPrice =
                    this.productPriceRepository.findFirstByProductAndIsActiveOrderByCreatedDateDesc(
                            orderDetail.getProduct(), true);
            orderDetail.setOrder(order);
            orderDetail.setProductPrice(productPrice);
        }
        this.orderRepository.save(order);
        this.orderDetailRepository.saveAll(order.getOrderDetails());
        OrderDto orderDto = this.orderMapper.toOrderDto(order);
        logService.loglama(EnumLogIslemTipi.OrdersAdd, securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {
        }
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult<>("ürün sepete eklendi", orderDto);
    }

    @LogMethodCall(value = "OrderUpdate is started")
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, timeout = 800)
    public DataResult<OrderDto> update(OrderUpdateRequest orderUpdateRequest) {
        Order orderToUpdate = this.orderRepository.getById(orderUpdateRequest.getOrderId());
        this.orderMapper.update(orderToUpdate, orderUpdateRequest);
        orderToUpdate.setModifiedDate(LocalDateTime.now());
        for (OrderDetail orderDetail : orderToUpdate.getOrderDetails()) {
            Optional<OrderDetailUpdateRequest> orderDetailUpdateRequest
                    = orderUpdateRequest.getOrderDetails()
                    .stream()
                    .filter(od -> od.getOrderDetailId().intValue() == orderDetail.getOrderDetailId().intValue())
                    .findFirst();
            if (orderDetailUpdateRequest.isPresent()) {
                this.orderDetailMapper.update(orderDetail, orderDetailUpdateRequest.get());
                orderDetail.setModifiedDate(LocalDateTime.now());
            }

        }
        this.orderRepository.save(orderToUpdate);
        this.orderDetailRepository.saveAll(orderToUpdate.getOrderDetails());

        OrderDto orderDto = this.orderMapper.toOrderDto(orderToUpdate);
        Method m = new Object() {
        }
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult<>("Sipariş güncellendi", orderDto);
    }

    @LogMethodCall(value = "OrderDelete is started")
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Result delete(OrderDeleteRequest orderDeleteRequest) {
        Optional<Order> optionalOrder = this.orderRepository.findById(orderDeleteRequest.getOrderId());
        if (!optionalOrder.isPresent()) {
            return new ErrorResult("Order not found");
        }
        Order orderToDelete = optionalOrder.get();
        orderToDelete.setStatus("DELETED");
        orderToDelete.setIsActive(false);
        orderToDelete.setModifiedDate(LocalDateTime.now());

        for (OrderDetail orderDetail : orderToDelete.getOrderDetails()) {
            orderDetail.setModifiedDate(LocalDateTime.now());
            orderDetail.setStatus("DELETED");
            orderDetail.setIsActive(false);
        }

        this.orderRepository.save(orderToDelete);
        this.orderDetailRepository.saveAll(orderToDelete.getOrderDetails());
        Method m = new Object() {
        }
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessResult("Sipariş silindi");
    }

    @LogMethodCall(value = "addProductToCart is started")
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public DataResult<OrderDto> addProductToCart(AddProductToCartRequest addProductToCartRequest) throws Exception {
        Order order =
                this.orderRepository
                        .findFirstByStatusAndIsActiveAndCustomer_CustomerIdOrderByCreatedDateDesc("Waiting", true, addProductToCartRequest.getCustomerId());
        if (order == null) {
            order = new Order();
            order.setStatus("Waiting");
            Customer customer = this.customerRepository.getById(addProductToCartRequest.getCustomerId());
            order.setCustomer(customer);
            User user = this.userRepository.getById(securityVerificationService.inquireLoggedInUser().getUserId());
            order.setUser(user);
            this.orderRepository.save(order);
        }
        List<OrderDetail> orderDetails = new ArrayList<>();
        Product product = this.productRepository.getById(addProductToCartRequest.getProductId());
        ProductPrice currentPrice =
                this.productPriceRepository.findFirstByProductAndIsActiveOrderByCreatedDateDesc(product, true);
        for (int i = 0; i < addProductToCartRequest.getCount(); i++) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setStatus("Confirmed");
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setProductPrice(currentPrice);

            orderDetails.add(orderDetail);
        }

        Method m = new Object() {
        }
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);

        this.orderDetailRepository.saveAll(orderDetails);
        //DataResult<OrderDto> orderDtoDataResult = this.getById(order.getOrderId());
        order = this.orderRepository.getById(order.getOrderId());
        return new SuccessDataResult<OrderDto>("Ürün sepete eklendi", this.orderMapper.toOrderDto(order));
    }
}
