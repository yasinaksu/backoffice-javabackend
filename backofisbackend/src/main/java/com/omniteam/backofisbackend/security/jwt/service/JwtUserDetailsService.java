package com.omniteam.backofisbackend.security.jwt.service;

import com.omniteam.backofisbackend.dto.user.UserDto;
import com.omniteam.backofisbackend.entity.User;
import com.omniteam.backofisbackend.repository.RoleRepository;
import com.omniteam.backofisbackend.repository.UserRepository;
import com.omniteam.backofisbackend.requests.user.UserAddRequest;
import com.omniteam.backofisbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByEmailAndIsActive(email,true);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        roleRepository.findRolesByUserId(user.getUserId()).forEach(role -> {
            grantedAuthorities.add(
                    new SimpleGrantedAuthority(role.getRoleName())
            );
        });
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                grantedAuthorities);
    }
}
