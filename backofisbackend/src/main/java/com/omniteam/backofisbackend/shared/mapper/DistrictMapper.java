package com.omniteam.backofisbackend.shared.mapper;

import com.omniteam.backofisbackend.dto.district.DistrictDto;
import com.omniteam.backofisbackend.entity.District;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
   componentModel = "spring"
)
public interface DistrictMapper {
    List<DistrictDto> toDistrictDtoList(List<District> districtList);
}
