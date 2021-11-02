package com.omniteam.backofisbackend.shared.mapper;

import com.omniteam.backofisbackend.dto.customer.CustomerAddContactsDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactAddDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactDto;
import com.omniteam.backofisbackend.dto.customercontact.CustomerContactUpdateDto;
import com.omniteam.backofisbackend.entity.CustomerContact;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface CustomerContactMapper {
    List<CustomerContact> toCustomerContactList(List<CustomerContactAddDto> customerContactAddDtoList);
    @Mapping(target = "country.countryId",source = "countryId")
    @Mapping(target = "city.cityId",source = "cityId") //expression ile dene
    @Mapping(target = "district.districtId",source = "districtId")
    CustomerContact toCustomerContact(CustomerContactAddDto contactAddDto);
    List<CustomerContactDto> customerContactDtoList(List<CustomerContact> customerContacts);


    @Mapping(target = "customerId",source = "customer.customerId")
    @Mapping(target = "countryName",source = "country.countryName")
    @Mapping(target = "cityName",source = "city.cityName")
    @Mapping(target = "districtName",source = "district.districtName")
    @Mapping(target = "countryId",source = "country.countryId")
    @Mapping(target = "cityId",source = "city.cityId")
    @Mapping(target = "districtId",source = "district.districtId")
    @Mapping(target = "isActive", expression = "java(customerContact.getIsActive())")
    CustomerContactDto toCustomerContactDto(CustomerContact customerContact);

//    @Mapping(target = "country.countryId",source = "countryId")
//    @Mapping(target = "city.cityId",source = "cityId") //expression ile dene
//    @Mapping(target = "district.districtId",source = "districtId")
//    @Mapping(target = "customerContactId", ignore = true)
    void update(@MappingTarget CustomerContact customerContact, CustomerContactUpdateDto customerContactUpdateDto);
}

