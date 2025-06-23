package com.fe_b17.simplenotes.controller;

import com.fe_b17.simplenotes.dto.DeleteAccountRequest;
import com.fe_b17.simplenotes.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/api/me")
public class UserController {
    private final UserService userService;

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteOwnAccount(@RequestBody @Valid DeleteAccountRequest dto) {
        userService.deleteCurrentAccount(dto.password());
        return ResponseEntity.noContent().build();
    }
}
