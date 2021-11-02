package com.omniteam.backofisbackend.shared.mapper;

import com.omniteam.backofisbackend.dto.role.RoleDto;
import com.omniteam.backofisbackend.entity.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface RoleMapper {
    RoleDto toRoleDto(Role role);
    List<RoleDto> toRoleDtoList(List<Role> roleList);
}
