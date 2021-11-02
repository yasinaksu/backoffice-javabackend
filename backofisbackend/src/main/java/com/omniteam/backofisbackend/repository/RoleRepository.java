package com.omniteam.backofisbackend.repository;


import com.omniteam.backofisbackend.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(
            value = "select r from UserRole ur, Role r where ur.role.roleId = r.roleId and ur.user.userId =:userId and r.isActive=true"
    )
    List<Role> findRolesByUserId(Integer userId);

    Page<Role> findAllBy(Pageable pageable);

    @Transactional
    List<Role> findAllByRoleIdIn(Set<Integer> roleIds);

    List<Role> findAllBy();

    Integer countAllBy();

    @Query( value = "from Role where (?1 is null or roleName like %?1%)",
            countQuery = "select count(roleId) from Role where (?1 is null or roleName like %?1%)")
    Page<Role> findAllByFilter(String searchText,Pageable pageable);

    Role findByRoleName(String name);



}
