package com.omniteam.backofisbackend.shared.mapper;

import com.omniteam.backofisbackend.dto.customer.CustomerAddDto;
import com.omniteam.backofisbackend.dto.customer.CustomerDto;
import com.omniteam.backofisbackend.dto.customer.CustomerUpdateDto;
import com.omniteam.backofisbackend.dto.customer.CustomerGetAllDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactAddDto;
import com.omniteam.backofisbackend.entity.Customer;
import com.omniteam.backofisbackend.entity.CustomerContact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface CustomerMapper {
    List<CustomerGetAllDto> toCustomerGetAllDtoList(List<Customer> customers);

    @Mapping(target = "customerContacts",source = "customerContactList")
    Customer toCustomer(CustomerAddDto customerAddDto);

    void update(@MappingTarget Customer customer, CustomerUpdateDto customerUpdateDto);

    @Mapping(target = "customerContactDtoList",source = "customerContacts")
    CustomerDto toCustomerDto(Customer customer);

    List<CustomerContact> toCustomerContactList(List<CustomerContactAddDto> customerContactAddDtoList);
    @Mapping(target = "country.countryId",source = "countryId")
    @Mapping(target = "city.cityId",source = "cityId") //expression ile dene
    @Mapping(target = "district.districtId",source = "districtId")
    CustomerContact toCustomerContact(CustomerContactAddDto contactAddDto);
}
