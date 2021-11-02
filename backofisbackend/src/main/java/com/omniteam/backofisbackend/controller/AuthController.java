package com.omniteam.backofisbackend.controller;

import com.omniteam.backofisbackend.dto.user.UserDto;
import com.omniteam.backofisbackend.entity.RefreshToken;
import com.omniteam.backofisbackend.entity.User;
import com.omniteam.backofisbackend.security.jwt.JwtTokenUtil;
import com.omniteam.backofisbackend.security.jwt.exception.TokenRefreshException;
import com.omniteam.backofisbackend.security.jwt.model.JwtRefreshRequest;
import com.omniteam.backofisbackend.security.jwt.model.JwtRefreshResponse;
import com.omniteam.backofisbackend.security.jwt.model.JwtRequest;
import com.omniteam.backofisbackend.security.jwt.model.JwtResponse;
import com.omniteam.backofisbackend.security.jwt.service.JwtUserDetailsService;
import com.omniteam.backofisbackend.service.RefreshTokenService;
import com.omniteam.backofisbackend.service.UserService;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.ErrorDataResult;
import com.omniteam.backofisbackend.shared.result.SuccessDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<DataResult<JwtResponse>> login(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        final String accessToken = jwtTokenUtil.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        return ResponseEntity.ok(new SuccessDataResult<>(new JwtResponse(accessToken,refreshToken.getToken())));
    }

    @PostMapping(path = "/refreshtoken")
    public ResponseEntity<JwtRefreshResponse> refreshtoken(@RequestBody JwtRefreshRequest request) throws Exception {
        String requestRefreshToken = request.getRefreshToken();
        Optional<User> willFindUser = refreshTokenService.findByToken(requestRefreshToken).map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser);
        if(willFindUser.isPresent()) {
            UserDto foundUser = this.userService.getByEmail(willFindUser.get().getEmail()).getData();
            return willFindUser.map(user -> {
                        String token = jwtTokenUtil.generateTokenFromUserDto(foundUser);
                        return ResponseEntity.ok(new JwtRefreshResponse(token, requestRefreshToken));
                    })
                    .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                            "Refresh token is not in database!"));
        }
        else throw new Exception("User not found");
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
