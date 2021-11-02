package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.entity.RefreshToken;
import com.omniteam.backofisbackend.entity.User;
import com.omniteam.backofisbackend.repository.RefreshTokenRepository;
import com.omniteam.backofisbackend.repository.UserRepository;
import com.omniteam.backofisbackend.security.jwt.exception.TokenRefreshException;
import com.omniteam.backofisbackend.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${jwt.refreshtoken.expiration.duration}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;
    public Optional<RefreshToken> findByToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByTokenAndIsActive(token,true);
        return refreshToken;
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public RefreshToken createRefreshToken(String email) {
        RefreshToken refreshToken = new RefreshToken();
        User user = userRepository.findUserByEmailAndIsActive(email,true);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        deleteByUserId(user.getUserId());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    public int deleteByUserId(Integer userId) {
        return this.refreshTokenRepository.updateIsActiveFalseByUserId(userId);
    }
}
