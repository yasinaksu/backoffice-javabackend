package com.omniteam.backofisbackend.shared.mapper;

import com.omniteam.backofisbackend.dto.city.CityDto;
import com.omniteam.backofisbackend.entity.City;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
    componentModel = "spring"
)
public interface CityMapper {
    List<CityDto> toCityDtoList(List<City> cityList);
}
