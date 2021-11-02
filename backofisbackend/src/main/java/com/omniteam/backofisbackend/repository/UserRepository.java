package com.omniteam.backofisbackend.repository;


import com.omniteam.backofisbackend.entity.User;
import com.omniteam.backofisbackend.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findUserByEmailAndIsActive(String email, Boolean isActive);
    User findUserByUserId(Integer userId);

    @Modifying
    @Query("update User u set u.userRoles = ?1")
    void setUserRoles(User user, Set<UserRole> userRoles);
    Page<User> findAllBy(Pageable pageable);

}
