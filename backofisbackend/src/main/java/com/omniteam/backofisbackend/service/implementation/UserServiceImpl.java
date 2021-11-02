package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.base.annotions.LogMethodCall;
import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.role.RoleDto;
import com.omniteam.backofisbackend.dto.user.UserDto;
import com.omniteam.backofisbackend.entity.Role;
import com.omniteam.backofisbackend.entity.User;
import com.omniteam.backofisbackend.entity.UserRole;
import com.omniteam.backofisbackend.enums.EnumLogIslemTipi;
import com.omniteam.backofisbackend.repository.*;
import com.omniteam.backofisbackend.repository.userspecification.UserSpec;
import com.omniteam.backofisbackend.requests.user.UserAddRequest;
import com.omniteam.backofisbackend.requests.user.UserUpdateRequest;
import com.omniteam.backofisbackend.service.RoleService;
import com.omniteam.backofisbackend.service.UserService;
import com.omniteam.backofisbackend.shared.mapper.UserMapper;
import com.omniteam.backofisbackend.shared.result.*;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private  SecurityVerificationServiceImpl securityVerificationService;
    @Autowired
    private  LogServiceImpl logService;

    @LogMethodCall
    @Override
    public DataResult<UserDto> getByEmail(String email) throws Exception {
        User user = this.userRepository.findUserByEmailAndIsActive(email, true);
        if (user == null) {
            throw new Exception("User not found");
            //throw exception //kullanıcı bulunamadı belki hata fırlatılabilir
        }
        UserDto userDto = this.userMapper.toUserDto(user);
        DataResult<List<RoleDto>> roleResult = this.roleService.getRolesByUserId(user.getUserId());
//        if (!roleResult.isSuccess()) {
//            //rolleri bulunamadı belki hata fırlatılabilir
//
//        }
        userDto.setRoles(roleResult.getData());
        return new SuccessDataResult<>(userDto);
    }

    @LogMethodCall(value = "UserAdd is started")
    @Transactional
    @Override
    public Result add(UserAddRequest userAddRequest) throws Exception {
            userAddRequest.setPassword(bcryptEncoder.encode(userAddRequest.getPassword()));
            User user = this.userMapper.toUserFromUserAddRequest(userAddRequest);
            if(userRepository.findUserByEmailAndIsActive(user.getEmail(),true)!=null)
                return new ErrorResult("Kullanıcı zaten mevcut.");
            if (userAddRequest.getCountryId() == null)
                user.setCountry(null);
            if (userAddRequest.getCityId() == null)
                user.setCity(null);
            if (userAddRequest.getDistrictId() == null)
                user.setDistrict(null);
            //to do kullanıcı parola hashleme işlemleri...
            user = this.userRepository.save(user);
            if (userAddRequest.getRoleIdList() != null) // TODO:  Role atama işlemleri
            {
                this.setUserRoles(user, userAddRequest.getRoleIdList().toArray(new Integer[userAddRequest.getRoleIdList().size()]));
            }
            logService.loglama(EnumLogIslemTipi.UserAdd,securityVerificationService.inquireLoggedInUser());
        //TODO return response'lar origin instance'lara çevirilmeli.
            return new SuccessResult(user.getUserId(),"Kullanıcı ekleme başarılı.");
            //return user;

    }

    @LogMethodCall(value = "UserUpdate is stated")
    @Transactional(propagation = Propagation.REQUIRED , rollbackFor = Exception.class,timeout = 800)
    @Override
    public Result update(Integer userId, UserUpdateRequest userUpdateRequest) throws Exception {
        if(userId==null)
            throw new Exception("UserId bilgisi boş bırakılamaz.");
        User existingUser = this.userRepository.findUserByUserId(userId);
        if(existingUser==null)
            throw new Exception("User bulunamadı!");

        this.userMapper.update(userUpdateRequest,existingUser);
        // Mapper'ın boş instance atamasının çözümü olarak;
        if (existingUser.getCountry().getCountryId() == null)
            existingUser.setCountry(null);
        if (existingUser.getCity().getCityId() == null)
            existingUser.setCity(null);
        if (existingUser.getDistrict().getDistrictId() == null)
            existingUser.setDistrict(null);

        this.userRepository.save(existingUser);
        this.userRoleRepository.deleteAllByUser(existingUser);
        if (userUpdateRequest.getRoleIdList() != null) // Role atama işlemleri
        {
            List<Role> wantingTheRolesToUser = this.roleRepository.findAllByRoleIdIn(userUpdateRequest.getRoleIdList());
            Set<UserRole> userRoles = wantingTheRolesToUser.stream().map(role -> {
                UserRole userRole = new UserRole();
                userRole.setUser(existingUser);
                userRole.setRole(role);
                return userRole;
            }).collect(Collectors.toSet());
            this.userRoleRepository.saveAll(userRoles);
        }
        logService.loglama(EnumLogIslemTipi.UserUpdate,securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {}
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall =  m.getAnnotation(LogMethodCall.class);
        return new SuccessResult(existingUser.getUserId(), "Kullanıcı başarıyla güncellendi.");
    }

    @LogMethodCall(value = "UserGetaAll is started")
    @Override
    public DataResult<PagedDataWrapper<UserDto>> getAll(Pageable pageable) {
        Page<User> userPage = this.userRepository.findAll(pageable);
        List<UserDto> userDtoList = this.userMapper.toUserDtoList(userPage.getContent());
        PagedDataWrapper<UserDto> userPagedWrapper = new PagedDataWrapper(userDtoList, userPage);
        logService.loglama(EnumLogIslemTipi.UsersGetAll,securityVerificationService.inquireLoggedInUser());
        Method m = new Object() {}
                .getClass()
                .getEnclosingMethod();

        LogMethodCall logMethodCall =  m.getAnnotation(LogMethodCall.class);
        return new SuccessDataResult(userPagedWrapper);
    }

    @Override
    public Result setUserRoles(User user, Role role) throws Exception {
        if (role == null)
            throw new Exception("Role cannot be null!");
        return this.setUserRoles(user, Arrays.asList(role));
    }

    @Override
    public Result setUserRoles(User user, List<Role> roles) throws Exception {
        if (user.getUserId() == null)
            throw new Exception("User henüz kaydedilmemiş.");

        Set<UserRole> userRoles = roles.stream().map(role -> {
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            return userRole;
        }).collect(Collectors.toSet());
        this.userRoleRepository.saveAll(userRoles);

        return new SuccessResult(user.getUserId(), "Kullanıcının rol seçimi kaydedildi.");

    }

    @Override
    public Result setUserRoles(User user, Integer[] roleIds) throws Exception {
        List<Role> roleList = roleRepository.findAllByRoleIdIn(new HashSet(Arrays.asList(roleIds)));
        return this.setUserRoles(user, roleList);
    }

    @Override
    public DataResult<List<UserDto>> search(String searchKey) {
        List<User> users = this.userRepository.findAll(UserSpec.searchUsersBySearchKey(searchKey));
        List<UserDto> userDtoList = this.userMapper.toUserDtoList(users);
        return new SuccessDataResult<>(0,userDtoList);
    }

}
