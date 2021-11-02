package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.base.annotions.LogMethodCall;
import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.customer.CustomerAddDto;
import com.omniteam.backofisbackend.dto.customer.CustomerDto;
import com.omniteam.backofisbackend.dto.customer.CustomerGetAllDto;
import com.omniteam.backofisbackend.dto.customer.CustomerUpdateDto;
import com.omniteam.backofisbackend.dto.order.OrderDto;
import com.omniteam.backofisbackend.entity.Customer;
import com.omniteam.backofisbackend.entity.CustomerContact;
import com.omniteam.backofisbackend.entity.Order;
import com.omniteam.backofisbackend.enums.EnumLogIslemTipi;
import com.omniteam.backofisbackend.repository.CustomerRepository;
import com.omniteam.backofisbackend.repository.OrderRepository;
import com.omniteam.backofisbackend.repository.UserRepository;
import com.omniteam.backofisbackend.service.CustomerService;
import com.omniteam.backofisbackend.service.SecurityVerificationService;
import com.omniteam.backofisbackend.shared.constant.ResultMessage;
import com.omniteam.backofisbackend.shared.mapper.CustomerMapper;
import com.omniteam.backofisbackend.shared.mapper.OrderMapper;
import com.omniteam.backofisbackend.shared.result.*;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityVerificationService securityVerificationService;

    @Autowired
    private LogServiceImpl logService;

    @LogMethodCall(value = "customerGetAll is started")
    @Cacheable(cacheNames = "CustomerGetAll")
    @Override
    public DataResult<PagedDataWrapper<CustomerGetAllDto>> getAll(int page, int size, String searchKey) throws InterruptedException {
        Thread.sleep(500L);
        Pageable pageable = PageRequest.of(page, size);
        if (searchKey == null) {
            searchKey = "";
        }
        /*Page<Customer> customers = this.customerRepository.findAll(pageable);*/
        Page<Customer> customers =
                this.customerRepository.findCustomersByFirstNameContainsOrLastNameContainsOrNationNumberContains(
                        searchKey,
                        searchKey,
                        searchKey,
                        pageable);

        List<CustomerGetAllDto> customerGetAllDtoList = customers.getNumberOfElements() == 0
                ? Collections.emptyList()
                : this.customerMapper.toCustomerGetAllDtoList(customers.getContent());

        PagedDataWrapper<CustomerGetAllDto> pagedDataWrapper = new PagedDataWrapper<>(
                customerGetAllDtoList,
                customers.getNumber(),
                customers.getSize(),
                customers.getTotalElements(),
                customers.getTotalPages(),
                customers.isLast()
        );
        logService.loglama(EnumLogIslemTipi.CustomersGetAll, securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {
        }.getClass().getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult<>(pagedDataWrapper);
    }


    @CacheEvict(cacheNames = "CustomerGetAll", allEntries = true)
    public void clearCustomerGetAllCache() {
        System.out.println("cache temizlendi");
    }

    @LogMethodCall(value = "CustomerAdd is started")
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Result add(CustomerAddDto customerAddDto) {
        Customer customer = this.customerMapper.toCustomer(customerAddDto);
        for (CustomerContact customerContact : customer.getCustomerContacts()) {
            customerContact.setCustomer(customer);
            if (
                    customerContact.getCity() == null ||
                    customerContact.getCity().getCityId() == null ||
                    customerContact.getCity().getCityId() == 0
            ) {
                customerContact.setCity(null);
                customerContact.setCountry(null);
                customerContact.setDistrict(null);
            }
        }
        this.customerRepository.save(customer);
        logService.loglama(EnumLogIslemTipi.CustomerAdd, securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {
        }
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessResult(ResultMessage.CUSTOMER_ADDED);
    }

    @LogMethodCall(value = "CustomerUpdate is started")
    @Override
    public Result update(CustomerUpdateDto customerUpdateDto) {
        Customer customerToUpdate = this.customerRepository.getById(customerUpdateDto.getCustomerId());
        this.customerMapper.update(customerToUpdate, customerUpdateDto);
        this.customerRepository.save(customerToUpdate);
        logService.loglama(EnumLogIslemTipi.CustomerUpdate, securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {}.getClass().getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessResult(ResultMessage.CUSTOMER_UPDATED);
    }

    @LogMethodCall(value = "CustomerGetById is started")
    @Override
    public DataResult<CustomerDto> getById(int customerId) {
        Customer customer = this.customerRepository.getById(customerId);
        CustomerDto customerDto = this.customerMapper.toCustomerDto(customer);
        logService.loglama(EnumLogIslemTipi.CustomerGetById, securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {}.getClass().getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult<>(customerDto);
    }

    @LogMethodCall(value = "getOrderByCustomerIdAndStatus is started")
    @Override
    public DataResult<OrderDto> getOrderByCustomerIdAndStatus(Integer customerId, String status) {
        Order order = this.orderRepository.findFirstByStatusAndIsActiveAndCustomer_CustomerIdOrderByCreatedDateDesc(status, true, customerId);
        if (order == null) {
            order = new Order();
            order.setStatus(status);
            order.setCustomer(customerRepository.getById(customerId));
            order.setUser(userRepository.getById(securityVerificationService.inquireLoggedInUser().getUserId()));
            this.orderRepository.save(order);
            return new SuccessDataResult<>(
                    this.orderMapper.toOrderDto(orderRepository.getById(order.getOrderId()))
            );
        }
        OrderDto orderDto = this.orderMapper.toOrderDto(order);
        Method m = new Object() {}.getClass().getEnclosingMethod();
        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult<OrderDto>(orderDto);
    }


}
