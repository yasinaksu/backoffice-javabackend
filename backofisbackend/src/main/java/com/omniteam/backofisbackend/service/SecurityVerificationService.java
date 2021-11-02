package com.omniteam.backofisbackend.service;

import com.omniteam.backofisbackend.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityVerificationService {

    public User inquireLoggedInUser();
}
