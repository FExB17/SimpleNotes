package com.fe_b17.simplenotes.user.controller;

import com.fe_b17.simplenotes.user.models.User;
import com.fe_b17.simplenotes.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;


    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

}
