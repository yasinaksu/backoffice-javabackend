package com.omniteam.backofisbackend.service;


import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.role.RoleDto;
import com.omniteam.backofisbackend.requests.RoleGetAllRequest;
import com.omniteam.backofisbackend.shared.result.DataResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    DataResult<List<RoleDto>> getRolesByUserId(Integer userId);
    DataResult<PagedDataWrapper<RoleDto>> getAllRoles(RoleGetAllRequest request);
}
