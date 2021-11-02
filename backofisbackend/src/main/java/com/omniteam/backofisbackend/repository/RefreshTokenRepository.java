package com.omniteam.backofisbackend.repository;

import com.omniteam.backofisbackend.entity.RefreshToken;
import com.omniteam.backofisbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByTokenAndIsActive(String token,Boolean isActive);

    @Modifying
    int deleteByUser(User user);

    @Modifying(clearAutomatically = true)
    @Query(value = "update RefreshToken rt set rt.isActive=false where rt.user.userId=:userId")
    int updateIsActiveFalseByUserId(Integer userId);

}
