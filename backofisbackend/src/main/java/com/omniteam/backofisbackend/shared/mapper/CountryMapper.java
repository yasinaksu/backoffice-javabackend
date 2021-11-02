package com.omniteam.backofisbackend.shared.mapper;

import com.omniteam.backofisbackend.dto.country.CountryDto;
import com.omniteam.backofisbackend.entity.Country;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface CountryMapper {
    List<CountryDto> toCountryDtoList(List<Country> countryList);
}
