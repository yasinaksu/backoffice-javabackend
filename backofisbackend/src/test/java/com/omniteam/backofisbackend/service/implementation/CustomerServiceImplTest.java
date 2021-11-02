package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.customer.CustomerAddDto;
import com.omniteam.backofisbackend.dto.customer.CustomerDto;
import com.omniteam.backofisbackend.dto.customer.CustomerGetAllDto;
import com.omniteam.backofisbackend.dto.customer.CustomerUpdateDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactAddDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactUpdateDto;
import com.omniteam.backofisbackend.dto.order.OrderDto;
import com.omniteam.backofisbackend.entity.Customer;
import com.omniteam.backofisbackend.entity.Order;
import com.omniteam.backofisbackend.entity.OrderDetail;
import com.omniteam.backofisbackend.entity.User;
import com.omniteam.backofisbackend.repository.CustomerRepository;
import com.omniteam.backofisbackend.repository.OrderRepository;
import com.omniteam.backofisbackend.repository.UserRepository;
import com.omniteam.backofisbackend.service.SecurityVerificationService;
import com.omniteam.backofisbackend.shared.mapper.CustomerMapper;
import com.omniteam.backofisbackend.shared.mapper.OrderMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.Result;
import com.omniteam.backofisbackend.shared.result.SuccessDataResult;
import com.omniteam.backofisbackend.shared.result.SuccessResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CustomerServiceImplTest {
    @Spy
    private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
    @Spy
    private CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityVerificationService securityVerificationService;
    @Mock
    private LogServiceImpl logService;
    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUserId(1);
        Mockito.when(
                this.securityVerificationService.inquireLoggedInUser()
        ).thenReturn(user);
        Mockito.when(
                this.logService.loglama(
                        Mockito.any(),
                        Mockito.any()
                )
        ).thenReturn(new SuccessResult());
    }

    @Test
    void getAll_ReturnsMoreElements_WhenSearchKeyNotExists() throws InterruptedException {
        List<Customer> customers = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Customer customer = new Customer();
            customer.setCustomerId(i);
            customers.add(customer);
        }
        Page<Customer> customerPage = new PageImpl<>(customers, PageRequest.of(0, 5), customers.size());
        Mockito.when(
                this.customerRepository
                        .findCustomersByFirstNameContainsOrLastNameContainsOrNationNumberContains(
                                Mockito.anyString(),
                                Mockito.anyString(),
                                Mockito.anyString(),
                                Mockito.any(Pageable.class)
                        )
        ).thenReturn(customerPage);


        DataResult<PagedDataWrapper<CustomerGetAllDto>> result = this.customerService.getAll(0, 5, null);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result.getData()).isNotNull();
        Assertions.assertThat(result.getData().getTotalElements()).isEqualTo(20);
        Assertions.assertThat(result.getData().getPage()).isEqualTo(0);
        Assertions.assertThat(result.getData().getSize()).isEqualTo(5);
        Assertions.assertThat(result.getData().getTotalPages()).isEqualTo(4);
        Assertions.assertThat(result.getData().isLast()).isFalse();
    }

    @Test
    void getAll_ReturnsLessElements_WhenSearchKeyExists() throws InterruptedException {
        List<Customer> customers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Customer customer = new Customer();
            customer.setCustomerId(i);
            customers.add(customer);
        }
        Page<Customer> customerPage = new PageImpl<>(customers, PageRequest.of(0, 5), customers.size());
        Mockito.when(
                this.customerRepository
                        .findCustomersByFirstNameContainsOrLastNameContainsOrNationNumberContains(
                                Mockito.anyString(),
                                Mockito.anyString(),
                                Mockito.anyString(),
                                Mockito.any(Pageable.class)
                        )
        ).thenReturn(customerPage);


        DataResult<PagedDataWrapper<CustomerGetAllDto>> result = this.customerService.getAll(0, 5, "search value");

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result.getData()).isNotNull();
        Assertions.assertThat(result.getData().getTotalElements()).isEqualTo(10);
        Assertions.assertThat(result.getData().getPage()).isEqualTo(0);
        Assertions.assertThat(result.getData().getSize()).isEqualTo(5);
        Assertions.assertThat(result.getData().getTotalPages()).isEqualTo(2);
        Assertions.assertThat(result.getData().isLast()).isFalse();
    }

    @Test
    void getAll_ReturnsZeroSizedList_WhenCustomersCountZero() throws InterruptedException {
        List<Customer> customers = new ArrayList<>();
        Page<Customer> customerPage = new PageImpl<>(customers, PageRequest.of(0, 5), customers.size());
        Mockito.when(
                this.customerRepository
                        .findCustomersByFirstNameContainsOrLastNameContainsOrNationNumberContains(
                                Mockito.anyString(),
                                Mockito.anyString(),
                                Mockito.anyString(),
                                Mockito.any(Pageable.class)
                        )
        ).thenReturn(customerPage);


        DataResult<PagedDataWrapper<CustomerGetAllDto>> result = this.customerService.getAll(0, 5, "search value");

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result.getData()).isNotNull();
        Assertions.assertThat(result.getData().getTotalElements()).isEqualTo(0);
        Assertions.assertThat(result.getData().getPage()).isEqualTo(0);
        Assertions.assertThat(result.getData().getSize()).isEqualTo(5);
        Assertions.assertThat(result.getData().getTotalPages()).isEqualTo(0);
        Assertions.assertThat(result.getData().isLast()).isTrue();
    }

    @Test
    void add() {
        Mockito.when(
                this.customerRepository.save(Mockito.any(Customer.class))
        ).thenReturn(new Customer());

        CustomerAddDto customerAddDto = new CustomerAddDto();
        List<CustomerContactAddDto> customerContactAddDtoList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            CustomerContactAddDto customerContactAddDto = new CustomerContactAddDto();
            if (i % 2 == 0) {
                customerContactAddDto.setCityId(i);
                customerContactAddDto.setDistrictId(i);
                customerContactAddDto.setCountryId(i);
            }
            customerContactAddDtoList.add(customerContactAddDto);
        }
        customerAddDto.setCustomerContactList(customerContactAddDtoList);
        Result result = this.customerService.add(customerAddDto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
    }

    @Test
    void update() {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        Mockito.when(
                this.customerRepository.getById(Mockito.any(Integer.class))
        ).thenReturn(customer);

        Mockito.when(
                this.customerRepository.save(Mockito.any(Customer.class))
        ).thenReturn(customer);

        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto();
        customerUpdateDto.setCustomerId(1);
        Result result = this.customerService.update(customerUpdateDto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result).isInstanceOf(SuccessResult.class);
    }

    @Test
    void getById() {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        Mockito.when(
                this.customerRepository.getById(Mockito.any(Integer.class))
        ).thenReturn(customer);

        DataResult<CustomerDto> result = this.customerService.getById(1);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result).isInstanceOf(SuccessDataResult.class);
    }

    @Test
    void getOrderByCustomerIdAndStatus_WhenOrderExist() {
        Order order = new Order();
        order.setOrderId(1);
        Mockito.when(
                this.orderRepository
                        .findFirstByStatusAndIsActiveAndCustomer_CustomerIdOrderByCreatedDateDesc(
                                Mockito.any(String.class),
                                Mockito.any(Boolean.class),
                                Mockito.any(Integer.class)
                        )
        ).thenReturn(order);


        DataResult<OrderDto> result = this.customerService.getOrderByCustomerIdAndStatus(1, "status");

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result).isInstanceOf(SuccessDataResult.class);
    }

    @Test
    void getOrderByCustomerIdAndStatus_WhenOrderNotExists() {
        Mockito.when(
                this.orderRepository
                        .findFirstByStatusAndIsActiveAndCustomer_CustomerIdOrderByCreatedDateDesc(
                                Mockito.any(String.class),
                                Mockito.any(Boolean.class),
                                Mockito.any(Integer.class)
                        )
        ).thenReturn(null);

        Mockito.when(
                customerRepository.getById(Mockito.anyInt())
        ).thenReturn(new Customer());

        Mockito.when(
                userRepository.getById(Mockito.anyInt())
        ).thenReturn(new User());

        Order order = new Order();
        order.setOrderId(1);
        Mockito.when(
                this.orderRepository.save(Mockito.any(Order.class))
        ).thenReturn(order);

        Mockito.when(
                this.orderRepository.getById(Mockito.any())
        ).thenReturn(order);

        DataResult<OrderDto> result = this.customerService.getOrderByCustomerIdAndStatus(1, "status");

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result).isInstanceOf(SuccessDataResult.class);
    }

    @Test
    void clearCustomerGetAllCache(){
        this.customerService.clearCustomerGetAllCache();
    }
}