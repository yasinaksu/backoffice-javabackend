package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.role.RoleDto;
import com.omniteam.backofisbackend.entity.Role;
import com.omniteam.backofisbackend.repository.RoleRepository;
import com.omniteam.backofisbackend.requests.RoleGetAllRequest;
import com.omniteam.backofisbackend.shared.mapper.RoleMapper;
import com.omniteam.backofisbackend.shared.mapper.RoleMapperImpl;
import com.omniteam.backofisbackend.shared.result.DataResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
public class RoleServiceImpTest {


    @Mock
    private RoleRepository roleRepository;

    @Spy
    private RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private LogServiceImpl logService;
    @Mock
    private SecurityVerificationServiceImpl securityVerificationService;


    @Test
    void getAllRolesWithoutPaginationTest() {

        List<Role> mockedRoles = new ArrayList<>();
        for (int i = 0; i < 15; i++)
            mockedRoles.add(new Role("role " + (i+1)));

        Mockito.when(roleRepository.findAllByFilter(null, PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(mockedRoles));

        RoleGetAllRequest roleGetAllRequest = new RoleGetAllRequest();
        DataResult<PagedDataWrapper<RoleDto>> returnedDataWithoutPagingAndFiltering = roleService.getAllRoles(roleGetAllRequest);
        assertEquals(15, returnedDataWithoutPagingAndFiltering.getData().getSize());
    }

    @Test
    void getAllRolesWithPaginationTest() {
        List<Role> mockedRoles = new ArrayList<>();
        for (int i = 0; i < 25; i++)
            mockedRoles.add(new Role("role " + (i+1)));

        Mockito.when(roleRepository.findAllByFilter(null, PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(mockedRoles.subList(0,20)));

        Mockito.when(roleRepository.findAllByFilter(null, PageRequest.of(1, 20)))
                .thenReturn(new PageImpl<>(mockedRoles.subList(20,25)));

        RoleGetAllRequest roleGetAllRequest = new RoleGetAllRequest();
        roleGetAllRequest.setPage(0);
        roleGetAllRequest.setSize(20);
        DataResult<PagedDataWrapper<RoleDto>> returnedDataWithPaging0 = roleService.getAllRoles(roleGetAllRequest);
        assertEquals(20, returnedDataWithPaging0.getData().getSize());
        roleGetAllRequest.setPage(1);
        DataResult<PagedDataWrapper<RoleDto>> returnedDataWithPaging1 = roleService.getAllRoles(roleGetAllRequest);
        assertEquals(5, returnedDataWithPaging1.getData().getSize());

    }


    @Test
    void getAllRolesWithSearchKeywordTest() {
        List<Role> mockedRoles = new ArrayList<>();
        for (int i = 0; i < 25; i++)
            mockedRoles.add(new Role("role " + (i+1)));

        Mockito.when(roleRepository.findAllByFilter("2", PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(mockedRoles.stream().filter(role -> role.getRoleName().equals("role 2")).collect(Collectors.toList())));

        Mockito.when(roleRepository.findAllByFilter("e%2%", PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(mockedRoles.stream().filter(role -> role.getRoleName().equals("role 21") || role.getRoleName().equals("role 2")).collect(Collectors.toList())));

        Mockito.when(roleRepository.findAllByFilter("e%21", PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(mockedRoles.stream().filter(role -> role.getRoleName().equals("role 21")).collect(Collectors.toList())));

        RoleGetAllRequest roleGetAllRequest = new RoleGetAllRequest();
        roleGetAllRequest.setSearch("2");
        DataResult<PagedDataWrapper<RoleDto>> returnedDataWithPagingAndFilter0 = roleService.getAllRoles(roleGetAllRequest);
        assertEquals(1, returnedDataWithPagingAndFilter0.getData().getSize());
        assertNotNull(returnedDataWithPagingAndFilter0.getData().getContent().get(0));
        assertEquals("role 2",returnedDataWithPagingAndFilter0.getData().getContent().get(0).getRoleName());
        roleGetAllRequest.setSearch("e 21");

        DataResult<PagedDataWrapper<RoleDto>> returnedDataWithPagingAndFilter1 = roleService.getAllRoles(roleGetAllRequest);
        assertEquals(1, returnedDataWithPagingAndFilter1.getData().getSize());
        assertNotNull(returnedDataWithPagingAndFilter1.getData().getContent().get(0));
        assertEquals("role 21",returnedDataWithPagingAndFilter1.getData().getContent().get(0).getRoleName());

        roleGetAllRequest.setSearch("e 2 ");
        DataResult<PagedDataWrapper<RoleDto>> returnedDataWithPagingAndFilter2 = roleService.getAllRoles(roleGetAllRequest);
        assertEquals(2, returnedDataWithPagingAndFilter2.getData().getSize());
        assertNotNull(returnedDataWithPagingAndFilter2.getData().getContent().get(0));
        assertNotNull(returnedDataWithPagingAndFilter2.getData().getContent().get(1));
        assertEquals("role 2",returnedDataWithPagingAndFilter2.getData().getContent().get(0).getRoleName());
        assertEquals("role 21",returnedDataWithPagingAndFilter2.getData().getContent().get(1).getRoleName());
    }


    @Test
    void getRolesByUserIdTest()
    {
        List<Role> mockedRoles = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            mockedRoles.add(new Role("role " + (i+1)));

        Mockito.when(roleRepository.findRolesByUserId(1)).thenReturn(mockedRoles);

        DataResult<List<RoleDto>> rolesByUserId = roleService.getRolesByUserId(1);
        assertNotNull(rolesByUserId);
        assertEquals(3,rolesByUserId.getData().size());
        assertNotNull(rolesByUserId.getData().get(0));
        assertNotNull(rolesByUserId.getData().get(1));
        assertNotNull(rolesByUserId.getData().get(2));
        assertEquals("role 1",rolesByUserId.getData().get(0).getRoleName());
        assertEquals("role 2",rolesByUserId.getData().get(1).getRoleName());
        assertEquals("role 3",rolesByUserId.getData().get(2).getRoleName());


    }




}
