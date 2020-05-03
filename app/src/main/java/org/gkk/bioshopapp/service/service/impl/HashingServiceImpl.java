package org.gkk.bioshopapp.service.service.impl;

import org.gkk.bioshopapp.service.service.HashingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

@Service
public class HashingServiceImpl implements HashingService {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public HashingServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String hash(String str) {
        return passwordEncoder.encode(str);
    }

    @Override
    public boolean isPasswordMatch(String inputPassword, String oldPassword) {
        return passwordEncoder.matches(inputPassword, oldPassword);
    }
}
