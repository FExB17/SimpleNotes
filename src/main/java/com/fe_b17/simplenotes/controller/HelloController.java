package com.fe_b17.simplenotes.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    private String hello(HttpServletRequest request) {
        return "Hello World! " + request.getSession().getId();
    }

}
