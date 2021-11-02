package com.omniteam.backofisbackend.service;

import com.omniteam.backofisbackend.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken createRefreshToken(String email);
    RefreshToken verifyExpiration(RefreshToken token);
    int deleteByUserId(Integer userId);
}
