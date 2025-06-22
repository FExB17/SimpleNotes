package com.fe_b17.simplenotes.controller;

import com.fe_b17.simplenotes.dto.*;
import com.fe_b17.simplenotes.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest dto, HttpServletRequest request) {
        return ResponseEntity.ok(authService.login(dto, request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest dto, HttpServletRequest request) {
        AuthResponse response = authService.registerUser(dto, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(HttpServletRequest request) {
        authService.logoutAll(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<SessionResponse>> getAllSessions(HttpServletRequest request) {
        return ResponseEntity.ok(authService.getActiveSessions(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshToken(@RequestBody RefreshRequest request){
        return ResponseEntity.ok(authService.refreshAccessToken(request.refreshToken()));

    }

    @PostMapping("/logout-device/{refreshTokenId}")
    public ResponseEntity<Void> logoutDevice(@PathVariable UUID refreshTokenId) {
        authService.logoutDevice(refreshTokenId);
        return ResponseEntity.noContent().build();
    }
}
