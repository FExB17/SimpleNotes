package com.fe_b17.simplenotes.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordEncoder passwordEncoder;

    public String hashPassword(String password){
        return passwordEncoder.encode(password);
    }

    public boolean checkPassword(String password, String hashedPassword){
        return passwordEncoder.matches(password, hashedPassword);
    }
}
