package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SecurityVerificationServiceImplTest {

    @InjectMocks
    private SecurityVerificationServiceImpl securityVerificationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest servletRequest;


    private User obtainTokenizedUser(Object principal) {
        return principal instanceof User ? (User) principal : null;
    }


    @Test
    public void inquireLoggedInUser() {
        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(auth.getPrincipal()).thenReturn(new User("tester", "123", new ArrayList<>()));
        com.omniteam.backofisbackend.entity.User user = new com.omniteam.backofisbackend.entity.User();
        user.setEmail("test@etiya");
        user.setPassword("123");
        user.setIsActive(true);


        UserDetails userDetails = new User("username", "password", true, true, true, true, auth.getAuthorities());
        assertNotNull(userDetails);

        //  Mockito.when(userRepository.findUserByEmailAndIsActive(user.getEmail(), user.getIsActive())).thenReturn(user);
        assertNotEquals(null, userDetails);
        //  assertEquals(user,userDetails);

        com.omniteam.backofisbackend.entity.User userService = securityVerificationService.inquireLoggedInUser();
        assertNull(userService);

        Mockito.when(securityContext.getAuthentication()).thenReturn(null);
        com.omniteam.backofisbackend.entity.User userServiceDenied = securityVerificationService.inquireLoggedInUser();
        assertNull(userServiceDenied);


        //   Assertions.assertThat(userrservice).isNotNull();
    }

    @Test
    void testObtainTokenizedUser() {
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return null;
            }
        };
        User user = securityVerificationService.obtainTokenizedUser(principal);

    }
}

  /*  public void inquireLoggedInUser() {
        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext secCont = Mockito.mock(SecurityContext.class);


        // Mockito.when(secCont.getAuthentication()).thenReturn(auth);
        // UserDetails userDetails = auth != null ? obtainTokenizedUser(auth.getPrincipal()) : null;
        com.omniteam.backofisbackend.entity.User user =new com.omniteam.backofisbackend.entity.User();
        //   Mockito.when(userRepository.findUserByEmailAndIsActive("test@etiya.com",true)).thenReturn(user);
        assertNotNull(user);*/