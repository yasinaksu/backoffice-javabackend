package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.role.RoleDto;
import com.omniteam.backofisbackend.dto.user.UserDto;
import com.omniteam.backofisbackend.entity.Role;
import com.omniteam.backofisbackend.entity.User;
import com.omniteam.backofisbackend.repository.RoleRepository;
import com.omniteam.backofisbackend.repository.UserRepository;
import com.omniteam.backofisbackend.repository.UserRoleRepository;
import com.omniteam.backofisbackend.requests.user.UserAddRequest;
import com.omniteam.backofisbackend.requests.user.UserUpdateRequest;
import com.omniteam.backofisbackend.shared.mapper.UserMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.ErrorResult;
import com.omniteam.backofisbackend.shared.result.Result;
import com.omniteam.backofisbackend.shared.result.SuccessResult;
import com.sun.org.apache.xpath.internal.operations.String;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;


    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Spy
    PasswordEncoder bcryptEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    private RoleServiceImpl roleService;

    @Mock
    private LogServiceImpl logService;
    @Mock
    private SecurityVerificationServiceImpl securityVerificationService;

    @Mock
    private UserRoleRepository userRoleRepository;


    //GetAll Tests
    @Test
    void getAllUsersTestWithoutPagingAsNull() {

        List<User> mockedUsers = new ArrayList<>();
        for (int i = 1; i <= 15; i++)
            mockedUsers.add(new User((i)));

//        Mockito.when(userRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(mockedUsers));
        Mockito.when(userRepository.findAll((Pageable) null)).thenReturn(new PageImpl<>(mockedUsers));
//        Mockito.when(userRepository.findAll(PageRequest.of(0,20))).thenReturn(new PageImpl<>(mockedUsers));

        DataResult<PagedDataWrapper<UserDto>> allUsers = userService.getAll(null);
        assertNotNull(allUsers);
        assertNotNull(allUsers.getData().getContent());


        for (int i = 1; i <= 15; i++) {
            Integer userId = i;
            assertTrue(allUsers.getData().getContent().stream().filter(user -> user.getUserId().equals(userId)).findFirst().isPresent());
        }


    }

    @Test
    void getAllUsersTestWithoutPagingAsDefault() {

        List<User> mockedUsers = new ArrayList<>();
        for (int i = 1; i <= 15; i++)
            mockedUsers.add(new User((i)));

        Mockito.when(userRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(mockedUsers));

        DataResult<PagedDataWrapper<UserDto>> allUsers = userService.getAll(Pageable.unpaged());
        assertNotNull(allUsers);
        assertNotNull(allUsers.getData().getContent());

        for (int i = 1; i <= 15; i++) {
            Integer userId = i;
            assertTrue(allUsers.getData().getContent().stream().filter(user -> user.getUserId().equals(userId)).findFirst().isPresent());
        }

    }

    @Test
    void getAllUsersTestWithPagingForOnePage() {

        List<User> mockedUsers = new ArrayList<>();
        for (int i = 1; i <= 15; i++)
            mockedUsers.add(new User((i)));

        Mockito.when(userRepository.findAll(PageRequest.of(0, 20))).thenReturn(new PageImpl<>(mockedUsers));

        DataResult<PagedDataWrapper<UserDto>> allUsers = userService.getAll(PageRequest.of(0, 20));
        assertNotNull(allUsers);
        assertNotNull(allUsers.getData().getContent());

        for (int i = 1; i <= 15; i++) {
            Integer userId = i;
            assertTrue(allUsers.getData().getContent().stream().filter(user -> user.getUserId().equals(userId)).findFirst().isPresent());
        }


    }

    @Test
    void getAllUsersTestWithPagingForMultiplePage() {

        List<User> mockedUsers = new ArrayList<>();
        for (int i = 1; i <= 25; i++)
            mockedUsers.add(new User((i)));

        Mockito.when(userRepository.findAll(PageRequest.of(0, 20))).thenReturn(new PageImpl<>(mockedUsers.subList(0, 20)));
        Mockito.when(userRepository.findAll(PageRequest.of(1, 20))).thenReturn(new PageImpl<>(mockedUsers.subList(20, 25)));

        DataResult<PagedDataWrapper<UserDto>> allUsersPage1 = userService.getAll(PageRequest.of(0, 20));
        assertNotNull(allUsersPage1);
        assertNotNull(allUsersPage1.getData().getContent());
        assertEquals(20, allUsersPage1.getData().getSize());

        DataResult<PagedDataWrapper<UserDto>> allUsersPage2 = userService.getAll(PageRequest.of(1, 20));
        assertNotNull(allUsersPage2);
        assertNotNull(allUsersPage2.getData().getContent());
        assertEquals(5, allUsersPage2.getData().getSize());

        for (int i = 1; i <= 20; i++) {
            Integer userId = i;
            assertTrue(allUsersPage1.getData().getContent().stream().filter(user -> user.getUserId().equals(userId)).findFirst().isPresent());
        }

        for (int i = 21; i <= 25; i++) {
            Integer userId = i;
            assertTrue(allUsersPage2.getData().getContent().stream().filter(user -> user.getUserId().equals(userId)).findFirst().isPresent());
        }


    }


    @Test
    void getUserByEmailTest() throws Exception {
        User user1 = new User(1);
        user1.setEmail("test1@email.com");
        User user2 = new User(2);
        user2.setEmail("test2@email.com");

        Mockito.when(userRepository.findUserByEmailAndIsActive("no@mail.com", true))
                .thenReturn(null);

        Mockito.when(userRepository.findUserByEmailAndIsActive("test1@email.com", true))
                .thenReturn(user1);
        Mockito.when(userRepository.findUserByEmailAndIsActive("test2@email.com", true))
                .thenReturn(user2);

        Mockito.when(this.roleService.getRolesByUserId(Mockito.anyInt()))
                .thenReturn(new DataResult(Collections.emptyList()));

        // Role 1 tanesi için ekleyelim
        Mockito.when(roleService.getRolesByUserId(2))
                .thenReturn(new DataResult<>(Arrays.asList(
                        new RoleDto(1, "role1"),
                        new RoleDto(2, "role2")
                )));


        Exception exception = assertThrows(Exception.class, () -> {
            userService.getByEmail("no@mail.com");
        });
        assertNotNull(exception);


        DataResult<UserDto> byEmail1 = userService.getByEmail("test1@email.com");
        assertNotNull(byEmail1);
        assertNotNull(byEmail1.getData());
        assertNotNull(byEmail1.getData().getUserId());
        assertNotNull(byEmail1.getData().getRoles());
        assertEquals(0, byEmail1.getData().getRoles().size());
        assertEquals(user1.getEmail(), byEmail1.getData().getEmail());

        DataResult<UserDto> byEmail2 = userService.getByEmail("test2@email.com");
        assertNotNull(byEmail2);
        assertNotNull(byEmail2.getData());
        assertNotNull(byEmail2.getData().getUserId());
        assertNotNull(byEmail2.getData().getRoles());
        assertEquals(2, byEmail2.getData().getRoles().size());

        assertEquals(1, byEmail2.getData().getRoles().get(0).getRoleId());
        assertEquals("role1", byEmail2.getData().getRoles().get(0).getRoleName());
        assertEquals(2, byEmail2.getData().getRoles().get(1).getRoleId());
        assertEquals("role2", byEmail2.getData().getRoles().get(1).getRoleName());

        assertEquals(user2.getEmail(), byEmail2.getData().getEmail());

    }

    @Test
    void setUserRolesTest() throws Exception {
        User user = new User(1);
        user.setEmail("test@email.com");

        Role role1 = new Role(1, "test1", null);
        Role role2 = new Role(2, "test2", null);

        Mockito.when(userRoleRepository.saveAll(Mockito.anyIterable())).thenReturn(new ArrayList<>());
        Mockito.when(roleRepository.findAllByRoleIdIn(Sets.newHashSet(Arrays.asList(1)))).thenReturn(Arrays.asList(role1));
        Mockito.when(roleRepository.findAllByRoleIdIn(Sets.newHashSet(Arrays.asList(2)))).thenReturn(Arrays.asList(role2));
        Mockito.when(roleRepository.findAllByRoleIdIn(Sets.newHashSet(Arrays.asList(1, 2)))).thenReturn(Arrays.asList(role1, role2));

        Exception exception = assertThrows(Exception.class, () -> {
            userService.setUserRoles(new User(), role1);
        });

        assertNotNull(exception);

        assertThrows(Exception.class, () -> userService.setUserRoles(new User(1), (Role) null));

        Result result = userService.setUserRoles(user, new Role("test1"));
        assertNotNull(result);
        assertNotNull(result.getMessage());
        assertTrue(result instanceof SuccessResult);
        assertEquals(user.getUserId(), result.getId());


        result = userService.setUserRoles(user, Arrays.asList(role1, role2));
        assertNotNull(result);
        assertNotNull(result.getMessage());
        assertTrue(result instanceof SuccessResult);
        assertEquals(user.getUserId(), result.getId());


        result = userService.setUserRoles(user, new Integer[]{1});
        assertNotNull(result);
        assertNotNull(result.getMessage());
        assertTrue(result instanceof SuccessResult);
        assertEquals(user.getUserId(), result.getId());

        result = userService.setUserRoles(user, new Integer[]{2});
        assertNotNull(result);
        assertNotNull(result.getMessage());
        assertTrue(result instanceof SuccessResult);
        assertEquals(user.getUserId(), result.getId());

        result = userService.setUserRoles(user, new Integer[]{1, 2});
        assertNotNull(result);
        assertNotNull(result.getMessage());
        assertTrue(result instanceof SuccessResult);
        assertEquals(user.getUserId(), result.getId());


    }


    @Test
    void addUserTest() throws Exception {
        HashSet roleIdHash = new HashSet<>(Arrays.asList(1, 2));
        Mockito.when(userRepository.findUserByEmailAndIsActive("test1@email.com", true))
                .thenReturn(new User(1));
        Mockito.when(userRepository.findUserByEmailAndIsActive("test2@email.com", true))
                .thenReturn(null);
        Mockito.when(roleRepository.findAllByRoleIdIn(roleIdHash)).thenReturn(Arrays.asList(new Role(1), new Role(2)));

        UserAddRequest userAddRequest = new UserAddRequest();

        assertThrows(Exception.class, () -> userService.add(userAddRequest));

        userAddRequest.setFirstName("unit");
        userAddRequest.setLastName("tester");
        userAddRequest.setEmail("test1@email.com");
        userAddRequest.setPassword(" ");
        userAddRequest.setRoleIdList(roleIdHash);
        Result result = userService.add(userAddRequest);
        assertNotNull(result);
        assertTrue(result instanceof ErrorResult);

        userAddRequest.setEmail("test2@email.com");


//        User willSaveUser = userMapper.toUserFromUserAddRequest(userAddRequest);

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(ans -> {
            User user = ans.getArgument(0);
            user.setUserId(1);
            return user;
        });
        result = userService.add(userAddRequest);
        assertNotNull(result);
        assertTrue(result instanceof SuccessResult);
        assertNotNull(result.getId());
        assertNotNull(result.getMessage());
        assertEquals(1, result.getId());


    }


    @Test
    void updateUserTest() throws Exception {
        assertThrows(Exception.class, () -> userService.update(null, new UserUpdateRequest()));
        assertThrows(Exception.class, () -> userService.update(15, new UserUpdateRequest()));
        HashSet roleIdHash = new HashSet<>(Arrays.asList(1, 2));

        User mockUser = new User(1);
        Mockito.when(userRepository.findUserByUserId(1))
                .thenReturn(mockUser);
        Mockito.when(roleRepository.findAllByRoleIdIn(roleIdHash)).thenReturn(Arrays.asList(new Role(1), new Role(2)));

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setFirstName("Non-Test");
        updateRequest.setLastName("Non-User");
        updateRequest.setRoleIdList(roleIdHash);
        updateRequest.setCityId(1);
        updateRequest.setCountryId(1);
        updateRequest.setDistrictId(1);


        Result updateResult = userService.update(1, updateRequest);

        assertNotNull(updateResult);
        assertNotNull(updateResult.getId());
        assertNotNull(updateResult.getMessage());
        assertEquals(1, updateResult.getId());

        //User update mapping test - Bug üzerine eklendi
        User updatedUser = new User();
        userMapper.update(updateRequest, updatedUser);
        assertNotNull(updatedUser);
        assertEquals(updateRequest.getFirstName(), updatedUser.getFirstName());
        assertEquals(updateRequest.getLastName(), updatedUser.getLastName());
        assertEquals(updateRequest.getDistrictId(), updatedUser.getDistrict().getDistrictId());
        assertEquals(updateRequest.getCityId(), updatedUser.getCity().getCityId());
        assertEquals(updateRequest.getCountryId(), updatedUser.getCountry().getCountryId());

        UserUpdateRequest updateRequestWithOnlyCity = new UserUpdateRequest();
        User willUpdateUserWithOnlyCity = new User(2);
        willUpdateUserWithOnlyCity.setFirstName("TestWithCity");
        willUpdateUserWithOnlyCity.setLastName("BugTest");
        updateRequestWithOnlyCity.setCityId(2);
        userMapper.update(updateRequestWithOnlyCity, willUpdateUserWithOnlyCity);
        assertEquals(2, willUpdateUserWithOnlyCity.getUserId());
        assertEquals("TestWithCity", willUpdateUserWithOnlyCity.getFirstName());
        assertEquals("BugTest", willUpdateUserWithOnlyCity.getLastName());


    }


    @Test
    void updateUserWithoutGeoInformationTest() throws Exception {
        assertThrows(Exception.class, () -> {
            userService.update(null, new UserUpdateRequest());
        });
        HashSet roleIdHash = new HashSet<>(Arrays.asList(1, 2));

        User mockUser = new User(1);
        Mockito.when(userRepository.findUserByUserId(1))
                .thenReturn(mockUser);
        Mockito.when(roleRepository.findAllByRoleIdIn(roleIdHash)).thenReturn(Arrays.asList(new Role(1), new Role(2)));

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setFirstName("Non-Test");
        updateRequest.setLastName("Non-User");
        updateRequest.setRoleIdList(roleIdHash);


        Result updateResult = userService.update(1, updateRequest);

        assertNotNull(updateResult);
        assertNotNull(updateResult.getId());
        assertNotNull(updateResult.getMessage());
        assertEquals(1, updateResult.getId());

        //User update mapping test - Bug üzerine eklendi
        User updatedUser = new User();
        userMapper.update(updateRequest, updatedUser);
        assertNotNull(updatedUser);
        assertEquals(updateRequest.getFirstName(), updatedUser.getFirstName());
        assertEquals(updateRequest.getLastName(), updatedUser.getLastName());
        assertEquals(null, updatedUser.getDistrict().getDistrictId());
        assertEquals(null, updatedUser.getCity().getCityId());
        assertEquals(null, updatedUser.getCountry().getCountryId());

        UserUpdateRequest updateRequestWithOnlyCity = new UserUpdateRequest();
        User willUpdateUserWithOnlyCity = new User(2);
        willUpdateUserWithOnlyCity.setFirstName("TestWithoutCity");
        willUpdateUserWithOnlyCity.setLastName("BugTest");
        updateRequestWithOnlyCity.setCityId(2);
        userMapper.update(updateRequestWithOnlyCity, willUpdateUserWithOnlyCity);
        assertEquals(2, willUpdateUserWithOnlyCity.getUserId());
        assertEquals("TestWithoutCity", willUpdateUserWithOnlyCity.getFirstName());
        assertEquals("BugTest", willUpdateUserWithOnlyCity.getLastName());


    }

    @Test
    void search() {
        List<User> userList = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setUserId(i);
            user.setEmail("test" + i + "@etiya.com");
            user.setFirstName("first name" + i);
            user.setLastName("last name" + i);
            user.setTitle("title" + i);
            userList.add(user);
        }
        Mockito.when(
                this.userRepository.findAll(Mockito.any(Specification.class))

        ).thenReturn(userList);

        DataResult<List<UserDto>> result = this.userService.search("keyword");

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result.getData()).isNotNull();
        Assertions.assertThat(result.getData()).hasSize(5);
    }


}
