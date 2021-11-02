package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.dto.customer.CustomerAddContactsDto;
import com.omniteam.backofisbackend.dto.customer.CustomerUpdateContactsDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactAddDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactUpdateDto;
import com.omniteam.backofisbackend.entity.*;
import com.omniteam.backofisbackend.repository.*;
import com.omniteam.backofisbackend.shared.mapper.CustomerContactMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.Result;
import com.omniteam.backofisbackend.shared.result.SuccessResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

public class CustomerContactServiceImplTest {
    @Mock
    private SecurityVerificationServiceImpl securityVerificationService;
    @Mock
    private LogServiceImpl logService;
    @Mock
    private CustomerContactRepository customerContactRepository;
    @Spy
    private CustomerContactMapper customerContactMapper = Mappers.getMapper(CustomerContactMapper.class);
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private DistrictRepository districtRepository;
    @InjectMocks
    private CustomerContactServiceImpl customerContactService;

    @BeforeEach
    void setUp() {
        //Mockito.ignoreStubs(this.logService,this.securityVerificationService);
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
    void getByCustomerId() {
        List<CustomerContact> customerContacts = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CustomerContact customerContact = new CustomerContact();
            customerContact.setCustomerContactId(1);
            customerContacts.add(customerContact);
        }
        Mockito.when(
                this.customerContactRepository
                        .findCustomerContactsByCustomer(Mockito.any(Customer.class))
        ).thenReturn(customerContacts);

        DataResult<List<CustomerContactDto>> result = this.customerContactService.getByCustomerId(1);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result.getData()).isNotNull();
        Assertions.assertThat(result.getData()).hasSize(5);
    }

    @Test
    void add() {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        Optional<Customer> optionalCustomer = Optional.of(customer);
        Mockito.when(
                this.customerRepository.findById(Mockito.any(Integer.class))
        ).thenReturn(optionalCustomer);

        List<CustomerContact> customerContacts = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            CustomerContact customerContact = new CustomerContact();
            customerContact.setCustomerContactId(i);
            customerContacts.add(customerContact);
        }
        Mockito.when(
                this.customerContactRepository.saveAll(Mockito.anyList())
        ).thenReturn(customerContacts);

        CustomerAddContactsDto customerAddContactsDto = new CustomerAddContactsDto();
        customerAddContactsDto.setCustomerId(1);
        List<CustomerContactAddDto> customerContactAddDtoList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            CustomerContactAddDto customerContactAddDto = new CustomerContactAddDto();
            customerContactAddDtoList.add(customerContactAddDto);
        }
        customerAddContactsDto.setCustomerContactAddDtoList(customerContactAddDtoList);
        Result result = this.customerContactService.add(customerAddContactsDto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result).isInstanceOf(SuccessResult.class);

    }

    @Test
    void update_Address() {
        CustomerContact customerContact = new CustomerContact();
        customerContact.setCustomerContactId(1);
        Mockito.when(
                this.customerContactRepository.getById(Mockito.any())
        ).thenReturn(customerContact);
        Country country = new Country();
        country.setCountryId(1);
        Mockito.when(
                this.countryRepository.getById(Mockito.any())
        ).thenReturn(country);

        City city = new City();
        city.setCityId(1);
        Mockito.when(
                this.cityRepository.getById(Mockito.any())
        ).thenReturn(city);

        District district =new District();
        district.setDistrictId(1);
        Mockito.when(
                this.districtRepository.getById(Mockito.any())
        ).thenReturn(district);

        Mockito.when(
                this.customerContactRepository.save(Mockito.any(CustomerContact.class))
        ).thenReturn(customerContact);

        CustomerUpdateContactsDto customerUpdateContactsDto = new CustomerUpdateContactsDto();
        List<CustomerContactUpdateDto> customerContactUpdateDtoList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            CustomerContactUpdateDto customerContactUpdateDto = new CustomerContactUpdateDto();
            customerContactUpdateDto.setCountryId(i);
            customerContactUpdateDto.setCityId(i);
            customerContactUpdateDto.setDistrictId(i);
            customerContactUpdateDtoList.add(customerContactUpdateDto);
        }
        customerUpdateContactsDto.setCustomerContactUpdateDtoList(customerContactUpdateDtoList);
        Result result = this.customerContactService.update(customerUpdateContactsDto);
    }

    @Test
    void update_PhoneOrEmail() {
        CustomerContact customerContact = new CustomerContact();
        customerContact.setCustomerContactId(1);
        Mockito.when(
                this.customerContactRepository.getById(Mockito.any())
        ).thenReturn(customerContact);

        Mockito.when(
                this.customerContactRepository.save(Mockito.any(CustomerContact.class))
        ).thenReturn(customerContact);

        CustomerUpdateContactsDto customerUpdateContactsDto = new CustomerUpdateContactsDto();
        List<CustomerContactUpdateDto> customerContactUpdateDtoList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            CustomerContactUpdateDto customerContactUpdateDto = new CustomerContactUpdateDto();
            customerContactUpdateDtoList.add(customerContactUpdateDto);
        }
        customerUpdateContactsDto.setCustomerContactUpdateDtoList(customerContactUpdateDtoList);
        Result result = this.customerContactService.update(customerUpdateContactsDto);
    }
}