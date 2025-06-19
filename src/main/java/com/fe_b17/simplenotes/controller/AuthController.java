package com.fe_b17.simplenotes.controller;

import com.fe_b17.simplenotes.dto.AuthResponse;
import com.fe_b17.simplenotes.dto.LoginRequest;
import com.fe_b17.simplenotes.dto.RegisterRequest;
import com.fe_b17.simplenotes.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest dto) {
        AuthResponse response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest dto) {
        AuthResponse response = authService.registerUser(dto);
        return ResponseEntity.ok(response);
    }



}
