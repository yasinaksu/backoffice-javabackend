package com.omniteam.backofisbackend.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.omniteam.backofisbackend.dto.role.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String title;
    private String email;
    @JsonIgnore
    private String password;
    private String phoneNumber;
    private String address;
    private Integer countryId;
    private Integer cityId;
    private Integer districtId;
    private List<RoleDto> roles=new ArrayList<>();
}
