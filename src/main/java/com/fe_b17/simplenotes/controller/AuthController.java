package com.fe_b17.simplenotes.controller;

import com.fe_b17.simplenotes.dto.LoginRequest;
import com.fe_b17.simplenotes.dto.RegisterRequest;
import com.fe_b17.simplenotes.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest dto) {
        String successMsg = userService.login(dto);
        return ResponseEntity.ok(successMsg);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest dto) {
        userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("user registered successfully");
    }



}
