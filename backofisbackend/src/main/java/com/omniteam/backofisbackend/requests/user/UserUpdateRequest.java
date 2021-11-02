package com.omniteam.backofisbackend.requests.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String title;
    private String email;
    private String phoneNumber;
    private String address;
    private Integer countryId;
    private Integer cityId;
    private Integer districtId;
    private Set<Integer> roleIdList;
}
