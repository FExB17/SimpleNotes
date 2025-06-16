package com.fe_b17.simplenotes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService {


    private final PasswordEncoder passwordEncoder;

    public boolean validatePassword(String password){
        // real validation logic
        return password != null && !password.isEmpty() && password.length() >= 8;
    }

    public String hashPassword(String password){
        return passwordEncoder.encode(password);
    }

    public boolean checkPassword(String password, String hashedPassword){
        return passwordEncoder.matches(password, hashedPassword);
    }

    public void registerPassword(String password){
        if (!validatePassword(password)){
            throw new IllegalArgumentException("Password does not meet the requirements");
        }
        String hashedPassword = hashPassword(password);


    }



}
