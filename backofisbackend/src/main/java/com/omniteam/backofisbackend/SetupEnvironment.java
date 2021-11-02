package com.omniteam.backofisbackend;

import com.omniteam.backofisbackend.entity.Role;
import com.omniteam.backofisbackend.entity.User;
import com.omniteam.backofisbackend.repository.RoleRepository;
import com.omniteam.backofisbackend.repository.UserRepository;
import com.omniteam.backofisbackend.repository.UserRoleRepository;
import com.omniteam.backofisbackend.requests.user.UserAddRequest;
import com.omniteam.backofisbackend.service.UserService;
import com.omniteam.backofisbackend.shared.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class SetupEnvironment {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @PostConstruct
    public void setupPost() throws Exception {
        System.out.println("Setup check is going to starting..");
        if (this.roleRepository.countAllBy() <= 0)
            setupStandartRoles();

//        if (userRepository.findUserByEmailAndIsActive("admin@etiya.com", true) == null)
//            setupAdminAccount();

    }

    public void setupStandartRoles() {
        List<Role> roleList = new ArrayList();
        roleList.add(new Role("Admin"));
        roleList.add(new Role("Seller"));
        roleList.add(new Role("Chief"));
        roleList.add(new Role("Manager"));
        roleList.add(new Role("Standard"));
        roleRepository.saveAll(roleList);
    }


    public void setupAdminAccount() throws Exception {
        Result userAddResult = userService.add(new UserAddRequest("System","Admin","admin@etiya.com","12345"));
        User user = userRepository.findUserByUserId(userAddResult.getId());
        userService.setUserRoles(user,roleRepository.findByRoleName("Admin"));
    }


}
