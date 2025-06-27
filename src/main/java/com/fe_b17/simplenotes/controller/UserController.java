package com.fe_b17.simplenotes.controller;

import com.fe_b17.simplenotes.dto.DeleteAccountRequest;
import com.fe_b17.simplenotes.dto.UserResponse;
import com.fe_b17.simplenotes.mapper.UserMapper;
import com.fe_b17.simplenotes.models.User;
import com.fe_b17.simplenotes.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/me")
public class UserController {
    private final UserService userService;



    @GetMapping()
    public ResponseEntity<UserResponse> getAccount() {
        User user = userService.getCurrentUser();
        UserResponse dto = UserMapper.toResponse(user);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteOwnAccount(@RequestBody @Valid DeleteAccountRequest dto) {
        userService.deleteCurrentAccount(dto.password());
        return ResponseEntity.noContent().build();
    }


}
