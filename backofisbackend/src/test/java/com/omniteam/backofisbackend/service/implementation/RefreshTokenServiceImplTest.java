package com.omniteam.backofisbackend.service.implementation;

import com.omniteam.backofisbackend.entity.RefreshToken;
import com.omniteam.backofisbackend.entity.User;
import com.omniteam.backofisbackend.repository.RefreshTokenRepository;
import com.omniteam.backofisbackend.repository.UserRepository;
import com.omniteam.backofisbackend.security.jwt.exception.TokenRefreshException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceImplTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenDurationMs", 30000000L);
    }

    @Test
    public void findByToken() {
        RefreshToken refreshToken = new RefreshToken();
        Optional<RefreshToken> optionalRefreshToken = Optional.of(refreshToken);

        Mockito.when(
                this.refreshTokenRepository.findByTokenAndIsActive(
                        Mockito.anyString(),
                        Mockito.anyBoolean()
                )
        ).thenReturn(optionalRefreshToken);

        Optional<RefreshToken> result = this.refreshTokenService.findByToken("test");
        Assertions.assertThat(result).isEqualTo(optionalRefreshToken);
        Assertions.assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void createRefreshToken() {
        User user = new User();
        user.setUserId(1);
        Mockito.when(
                this.userRepository.findUserByEmailAndIsActive(Mockito.anyString(), Mockito.anyBoolean())
        ).thenReturn(user);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        Mockito.when(
                this.refreshTokenRepository.updateIsActiveFalseByUserId(Mockito.anyInt())
        ).thenReturn(1);
        Mockito.when(
                this.refreshTokenService.deleteByUserId(Mockito.anyInt())
        ).thenReturn(1);
        Mockito.when(
                this.refreshTokenRepository.save(Mockito.any(RefreshToken.class))
        ).thenReturn(refreshToken);
        RefreshToken result = this.refreshTokenService.createRefreshToken("test@test.com");

        Assertions.assertThat(result).isEqualTo(refreshToken);
        Assertions.assertThat(result.getToken()).isEqualTo(refreshToken.getToken());
    }

    @Test
    public void verifyExpiration_ReturnsVerifiedRefreshToken_IfGivenNotExpired() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshTokenId(1);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(1000));


        RefreshToken result = this.refreshTokenService.verifyExpiration(refreshToken);
    }

    @Test
    public void verifyExpiration_ThrowsTokenRefreshException_IfGivenExpired() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshTokenId(1);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(-1000));
        Mockito.doNothing().when(this.refreshTokenRepository).delete(refreshToken);
        Assertions.assertThatThrownBy(() -> {
                    this.refreshTokenService.verifyExpiration(refreshToken);
                })
                .isInstanceOf(TokenRefreshException.class)
                .hasMessageContaining("Refresh token was expired. Please make a new signin request");
    }

    @Test
    public void deleteByUserId() {
        Mockito.when(
                this.refreshTokenRepository.updateIsActiveFalseByUserId(Mockito.anyInt())
        ).thenReturn(1);

        int result = this.refreshTokenService.deleteByUserId(1);
        Assertions.assertThat(result).isEqualTo(1);
    }
}