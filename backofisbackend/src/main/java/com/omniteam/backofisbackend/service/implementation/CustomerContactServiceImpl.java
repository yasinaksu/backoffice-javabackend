package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.base.annotions.LogMethodCall;
import com.omniteam.backofisbackend.dto.customer.CustomerAddContactsDto;
import com.omniteam.backofisbackend.dto.customer.CustomerUpdateContactsDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactUpdateDto;
import com.omniteam.backofisbackend.entity.*;
import com.omniteam.backofisbackend.enums.EnumLogIslemTipi;
import com.omniteam.backofisbackend.repository.*;
import com.omniteam.backofisbackend.service.CustomerContactService;
import com.omniteam.backofisbackend.shared.constant.ResultMessage;
import com.omniteam.backofisbackend.shared.mapper.CustomerContactMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.Result;
import com.omniteam.backofisbackend.shared.result.SuccessDataResult;
import com.omniteam.backofisbackend.shared.result.SuccessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerContactServiceImpl implements CustomerContactService {
    @Autowired
    private SecurityVerificationServiceImpl securityVerificationService;
    @Autowired
    private LogServiceImpl logService;

    @Autowired
    private CustomerContactRepository customerContactRepository;
    @Autowired
    private CustomerContactMapper customerContactMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private CountryRepository countryRepository;

    @LogMethodCall(value = "getByCustomerId is started")
    @Override
    public DataResult<List<CustomerContactDto>> getByCustomerId(int customerId) {
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        List<CustomerContact> customerContacts = this.customerContactRepository.findCustomerContactsByCustomer(customer);
        List<CustomerContactDto> customerContactDtoList = this.customerContactMapper.customerContactDtoList(customerContacts);
        logService.loglama(EnumLogIslemTipi.CustomerGetContacts, securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {
        }
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult<>(customerContactDtoList);
    }

    @LogMethodCall(value = "CustomerContactAdd is started")
    @Override
    public Result add(CustomerAddContactsDto customerAddContactsDto) {
        List<CustomerContact> customerContactList =
                this.customerContactMapper.toCustomerContactList(customerAddContactsDto.getCustomerContactAddDtoList());
        Optional<Customer> customerOptional = this.customerRepository.findById(customerAddContactsDto.getCustomerId());

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            for (CustomerContact customerContact : customerContactList) {
                customerContact.setCustomer(customer);
                if (customerContact.getCity() == null || customerContact.getCity().getCityId() == null) {
                    customerContact.setCity(null);
                    customerContact.setCountry(null);
                    customerContact.setDistrict(null);
                }
            }
        }

        this.customerContactRepository.saveAll(customerContactList);
        logService.loglama(EnumLogIslemTipi.CustomerAddContacts, securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {
        }.getClass().getEnclosingMethod();
        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessResult("contacts added");
    }

    @LogMethodCall(value = "CustomerContactUpdated is started")
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Result update(CustomerUpdateContactsDto customerUpdateContactsDto) {
        List<CustomerContactUpdateDto> customerContactUpdateDtoList = customerUpdateContactsDto.getCustomerContactUpdateDtoList();
        for (CustomerContactUpdateDto customerContactUpdateDto : customerContactUpdateDtoList){
            CustomerContact customerContactToUpdate = this.customerContactRepository.getById(customerContactUpdateDto.getCustomerContactId());
            this.customerContactMapper.update(customerContactToUpdate, customerContactUpdateDto);
            if (customerContactUpdateDto.getCityId() != null && customerContactUpdateDto.getCityId() != 0) {
                City city = this.cityRepository.getById(customerContactUpdateDto.getCityId());
                customerContactToUpdate.setCity(city);
            }
            if (customerContactUpdateDto.getCountryId() != null && customerContactUpdateDto.getCountryId() != 0) {
                Country country = this.countryRepository.getById(customerContactUpdateDto.getCountryId());
                customerContactToUpdate.setCountry(country);
            }
            if (customerContactUpdateDto.getDistrictId() != null && customerContactUpdateDto.getDistrictId() != 0) {
                District district = this.districtRepository.getById(customerContactUpdateDto.getDistrictId());
                customerContactToUpdate.setDistrict(district);
            }
            this.customerContactRepository.save(customerContactToUpdate);
        }

        logService.loglama(EnumLogIslemTipi.CustomerUpdateContacts, securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {}.getClass().getEnclosingMethod();
        LogMethodCall logMethodCall = m.getAnnotation(LogMethodCall.class);
        return new SuccessResult(ResultMessage.CUSTOMER_CONTACT_UPDATED);
    }
}
