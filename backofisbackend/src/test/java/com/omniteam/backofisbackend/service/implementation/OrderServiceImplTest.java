package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.order.AddProductToCartRequest;
import com.omniteam.backofisbackend.dto.order.OrderDto;
import com.omniteam.backofisbackend.entity.*;
import com.omniteam.backofisbackend.repository.*;
import com.omniteam.backofisbackend.requests.order.*;
import com.omniteam.backofisbackend.service.SecurityVerificationService;
import com.omniteam.backofisbackend.shared.mapper.OrderDetailMapper;
import com.omniteam.backofisbackend.shared.mapper.OrderMapper;
import com.omniteam.backofisbackend.shared.result.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductPriceRepository productPriceRepository;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private LogServiceImpl logService;
    @Mock
    private SecurityVerificationService securityVerificationService;
    @Mock
    private JobLauncher jobLauncher;
    @Mock
    private Job orderExporterJob;
    @Spy
    private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
    @Spy
    private OrderDetailMapper orderDetailMapper = Mappers.getMapper(OrderDetailMapper.class);

    @InjectMocks
    OrderServiceImpl orderService;


    @Test
    void textContextLoadTest() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        assertNotNull(orderService);
        assertNotNull(orderMapper);
    }


    @Test
    void orderGetByIdTest() throws Exception {
        Integer orderId = 1;
        Order mockedOrder = new Order(orderId);
        mockedOrder.setStatus("COMPLETED");
        mockedOrder.setCustomer(new Customer());
        mockedOrder.setUser(new User());
        //securityVerificationService.inquireLoggedInUser()
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockedOrder));
        Mockito.when(orderRepository.findById(orderId + 1)).thenReturn(Optional.ofNullable(null));

        assertThrows(Exception.class, () -> orderService.getById(orderId + 1));
        DataResult<OrderDto> orderGetById = orderService.getById(orderId);
        assertNotNull(orderGetById);
        assertNotNull(orderGetById.getData());
        assertNotNull(orderGetById.getId());
        assertEquals(mockedOrder.getOrderId(), orderGetById.getId());
        assertEquals(mockedOrder.getOrderId(), orderGetById.getData().getOrderId());
        assertEquals(mockedOrder.getStatus(), orderGetById.getData().getStatus());
    }

    @Test
    void getById() throws Exception {
        Optional<Order> optionalOrder = Optional.of(new Order());
        Mockito.when(
                this.orderRepository.findById(Mockito.anyInt())
        ).thenReturn(optionalOrder);

        DataResult<OrderDto> result = this.orderService.getById(1);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result.getData()).isNotNull();
    }

    @Test
    void getAll() {
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            orders.add(new Order());
        }
        Page<Order> orderPage = new PageImpl<Order>(orders, PageRequest.of(0, 5), 10);

        Mockito.when(
                this.orderRepository.findAll(
                        Mockito.any(Specification.class),
                        Mockito.any(Pageable.class)
                )
        ).thenReturn(orderPage);

        DataResult<PagedDataWrapper<OrderDto>> result = this.orderService.getAll(new OrderGetAllRequest());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result.getData().getContent()).isNotNull();
        Assertions.assertThat(result.getData().getContent()).hasSize(10);
        Assertions.assertThat(result.getData().getSize()).isEqualTo(5);
        Assertions.assertThat(result.getData().getPage()).isEqualTo(0);
        Assertions.assertThat(result.getData().isLast()).isFalse();
        Assertions.assertThat(result.getData().getTotalPages()).isEqualTo(2);
        Assertions.assertThat(result.getData().getTotalElements()).isEqualTo(10);
    }


    @Test
    void add(){
        OrderAddRequest orderAddRequest = new OrderAddRequest();
        orderAddRequest.setCustomerId(1);
        orderAddRequest.setStatus("status");
        orderAddRequest.setUserId(1);
        List<OrderDetailAddRequest> orderDetailAddRequests = new ArrayList<>();
        for (int i = 0; i<3; i++){
            OrderDetailAddRequest orderDetailAddRequest = new OrderDetailAddRequest();
            orderDetailAddRequest.setStatus("status "+i);
            orderDetailAddRequest.setProductId(i+1);
            orderDetailAddRequests.add(orderDetailAddRequest);
        }
        orderAddRequest.setOrderDetails(orderDetailAddRequests);
        Mockito.when(
                this.productPriceRepository.findFirstByProductAndIsActiveOrderByCreatedDateDesc(
                        Mockito.any(Product.class),
                        Mockito.anyBoolean()
                )
        ).thenReturn(new ProductPrice());

        Mockito.when(
                this.orderRepository.save(Mockito.any(Order.class))
        ).thenReturn(new Order());

        Mockito.when(
                this.orderDetailRepository.saveAll(Mockito.anyList())
        ).thenReturn(Arrays.asList(new OrderDetail()));

        DataResult<OrderDto> result = this.orderService.add(orderAddRequest);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void update(){
        Order order = new Order();
        order.setOrderId(1);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (int i =1; i<4; i++){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderDetailId(i);
            orderDetail.setStatus("güncelleme "+i);
        }
        order.setOrderDetails(orderDetailList);
        Mockito.when(
                this.orderRepository.getById(Mockito.anyInt())
        ).thenReturn(order);

        Mockito.when(
                this.orderRepository.save(Mockito.any(Order.class))
        ).thenReturn(order);

        Mockito.when(
        this.orderDetailRepository.saveAll(Mockito.anyList())
        ).thenReturn(order.getOrderDetails());


        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();
        orderUpdateRequest.setOrderId(1);
        orderUpdateRequest.setStatus("güncellendi");
        List<OrderDetailUpdateRequest> orderDetailUpdateRequestList = new ArrayList<>();
        for (int i =1; i<4; i++){
            OrderDetailUpdateRequest orderDetailUpdateRequest = new OrderDetailUpdateRequest();
            orderDetailUpdateRequest.setOrderDetailId(i);
            orderDetailUpdateRequest.setStatus("güncelleme "+i);
        }
        orderUpdateRequest.setOrderDetails(orderDetailUpdateRequestList);
        DataResult<OrderDto> result = this.orderService.update(orderUpdateRequest);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void delete_ReturnsSuccessResult_WhenOrderFound(){
        Optional<Order> optionalOrder = Optional.of(new Order());
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (int i=1;i<=5;i++){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderDetailId(i);
        }
        optionalOrder.get().setOrderDetails(orderDetails);

        Mockito.when(
                this.orderRepository.findById(Mockito.any())
        ).thenReturn(optionalOrder);

        Mockito.when(
                this.orderRepository.save(Mockito.any(Order.class))
        ).thenReturn(optionalOrder.get());

        Mockito.when(
                this.orderDetailRepository.saveAll(Mockito.anyList())
        ).thenReturn(orderDetails);

        Result result = this.orderService.delete(new OrderDeleteRequest());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result).isInstanceOf(SuccessResult.class);
    }

    @Test
    void delete_ReturnsErrorResult_WhenOrderNotFound(){
        Optional<Order> optionalOrder = Optional.empty();
        Result result = this.orderService.delete(new OrderDeleteRequest());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isFalse();
        Assertions.assertThat(result).isInstanceOf(ErrorResult.class);
    }

    @Test
    void addProductToCart_CreatesNewOrder_IfOrderExist() throws Exception{
        Order order = new Order();
        Mockito.when(
                this.orderRepository
                        .findFirstByStatusAndIsActiveAndCustomer_CustomerIdOrderByCreatedDateDesc(
                                Mockito.any(String.class),
                                Mockito.any(Boolean.class),
                                Mockito.any(Integer.class)
                        )
        ).thenReturn(order);
        Mockito.when(
                this.productRepository.getById(Mockito.any())
        ).thenReturn(new Product());

        Mockito.when(
                this.productPriceRepository
                        .findFirstByProductAndIsActiveOrderByCreatedDateDesc(
                                Mockito.any(Product.class),
                                Mockito.anyBoolean()
                        )
        ).thenReturn(new ProductPrice());

        Mockito.when(
                this.orderDetailRepository.saveAll(Mockito.anyList())
        ).thenReturn(Collections.singletonList(new OrderDetail()));

        Mockito.when(
                this.orderRepository.getById(Mockito.any())
        ).thenReturn(order);

        AddProductToCartRequest addProductToCartRequest = new AddProductToCartRequest(
                1,
                1,
                10
        );

        DataResult<OrderDto> result = this.orderService.addProductToCart(addProductToCartRequest);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void addProductToCart_CreatesNewOrder_IfOrderNotExist() throws Exception{
        Mockito.when(
                this.orderRepository
                        .findFirstByStatusAndIsActiveAndCustomer_CustomerIdOrderByCreatedDateDesc(
                                Mockito.any(String.class),
                                Mockito.any(Boolean.class),
                                Mockito.any(Integer.class)
                        )
        ).thenReturn(null);

        Customer customer = new Customer();
        customer.setCustomerId(1);
        Mockito.when(
                this.customerRepository.getById(Mockito.any())
        ).thenReturn(customer);

        User user = new User();
        user.setUserId(1);
        Mockito.when(
                securityVerificationService.inquireLoggedInUser()
        ).thenReturn(user);
        Mockito.when(
                this.userRepository.getById(Mockito.any())
        ).thenReturn(user);

        Order order = new Order();
        order.setOrderId(1);
        Mockito.when(
                this.orderRepository.save(Mockito.any(Order.class))
        ).thenReturn(order);

        Mockito.when(
                this.productRepository.getById(Mockito.any())
        ).thenReturn(new Product());

        Mockito.when(
                this.productPriceRepository
                        .findFirstByProductAndIsActiveOrderByCreatedDateDesc(
                                Mockito.any(Product.class),
                                Mockito.anyBoolean()
                        )
        ).thenReturn(new ProductPrice());

        Mockito.when(
                this.orderDetailRepository.saveAll(Mockito.anyList())
        ).thenReturn(Collections.singletonList(new OrderDetail()));

        Mockito.when(
                this.orderRepository.getById(Mockito.any())
        ).thenReturn(order);

        AddProductToCartRequest addProductToCartRequest = new AddProductToCartRequest(
                1,
                1,
                10
        );

        DataResult<OrderDto> result = this.orderService.addProductToCart(addProductToCartRequest);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
    }
}