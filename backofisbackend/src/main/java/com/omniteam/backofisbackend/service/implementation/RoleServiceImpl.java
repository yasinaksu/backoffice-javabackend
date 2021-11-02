package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.base.annotions.LogMethodCall;
import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.role.RoleDto;
import com.omniteam.backofisbackend.entity.Role;
import com.omniteam.backofisbackend.enums.EnumLogIslemTipi;
import com.omniteam.backofisbackend.repository.RoleRepository;
import com.omniteam.backofisbackend.requests.RoleGetAllRequest;
import com.omniteam.backofisbackend.service.RoleService;
import com.omniteam.backofisbackend.shared.mapper.RoleMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.SuccessDataResult;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

@Service
@NoArgsConstructor
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    private SecurityVerificationServiceImpl securityVerificationService;

    @Autowired
    private  LogServiceImpl logService;


    @LogMethodCall(value = "getRolesByUserId is started")
    @Override
    public DataResult<List<RoleDto>> getRolesByUserId(Integer userId) {
        logService.loglama(EnumLogIslemTipi.GetRoleByUser,securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {}
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall =  m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult<>(
                this.roleMapper.toRoleDtoList(this.roleRepository.findRolesByUserId(userId))

        );
    }

    @LogMethodCall(value = "getAllRoles is stated")
    @Override
    public DataResult<PagedDataWrapper<RoleDto>> getAllRoles(RoleGetAllRequest request) {
        if(request.getPage()==null)
            request.setPage(0);
        if(request.getSize()==null)
            request.setSize(20);
        Pageable pageable = PageRequest.of(request.getPage(),request.getSize());
        String searchText = (request.getSearch()==null) ? null : request.getSearch().replace(" ","%");
        Page<Role> rolePage = this.roleRepository.findAllByFilter(searchText,pageable);
       List<RoleDto> roleDtoList = this.roleMapper.toRoleDtoList(rolePage.getContent());
        PagedDataWrapper<RoleDto> rolePagedWrapper = new PagedDataWrapper(roleDtoList,rolePage);
        logService.loglama(EnumLogIslemTipi.GetAllRoles,securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {}
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall =  m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult(rolePagedWrapper);
    }
}
