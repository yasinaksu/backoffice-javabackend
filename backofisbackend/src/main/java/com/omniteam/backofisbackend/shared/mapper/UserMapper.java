package com.omniteam.backofisbackend.shared.mapper;

import com.omniteam.backofisbackend.dto.user.UserDto;
import com.omniteam.backofisbackend.entity.User;
import com.omniteam.backofisbackend.requests.user.UserAddRequest;
import com.omniteam.backofisbackend.requests.user.UserUpdateRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class,DistrictMapper.class,CityMapper.class,CountryMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE

)
public interface UserMapper {
    @Mapping(target = "countryId", source = "country.countryId")
    @Mapping(target = "cityId", source = "city.cityId")
    @Mapping(target = "districtId", source = "district.districtId")
    UserDto toUserDto(User user);

    @Mapping(target = "country.countryId", source = "countryId")
    @Mapping(target = "city.cityId", source = "cityId")
    @Mapping(target = "district.districtId", source = "districtId")
    User toUserFromUserAddRequest(UserAddRequest userAddRequest);


    @Mapping(target = "country.countryId", source = "countryId")
    @Mapping(target = "city.cityId", source = "cityId")
    @Mapping(target = "district.districtId", source = "districtId")
    List<User> toUserList(List<UserDto> userDtoList);

    @Mapping(source = "country.countryId", target = "countryId")
    @Mapping(source = "city.cityId", target = "cityId")
    @Mapping(source = "district.districtId", target = "districtId")
    List<UserDto> toUserDtoList(List<User> userList);


    @Mapping(target = "country.countryId", source = "countryId")
    @Mapping(target = "city.cityId", source = "cityId")
    @Mapping(target = "district.districtId", source = "districtId")
    void update(UserUpdateRequest userUpdateRequest, @MappingTarget User user);

}
