package com.omniteam.backofisbackend.service;

import com.omniteam.backofisbackend.dto.user.UserDto;
import com.omniteam.backofisbackend.entity.Role;
import com.omniteam.backofisbackend.entity.User;
import com.omniteam.backofisbackend.requests.user.UserAddRequest;
import com.omniteam.backofisbackend.requests.user.UserUpdateRequest;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.Result;
import com.omniteam.backofisbackend.shared.result.SuccessResult;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    DataResult<UserDto> getByEmail(String email) throws Exception;
    Result add(UserAddRequest userAddRequest) throws Exception;
    Result update(Integer userId,UserUpdateRequest userUpdateRequest) throws Exception;
    Result getAll(Pageable pageable);
    Result setUserRoles(User user,Role role) throws Exception;
    Result setUserRoles(User user,List<Role> roles) throws Exception;
    Result setUserRoles(User user,Integer[] roleIds) throws Exception;
    DataResult<List<UserDto>> search(String searchKey);
}
