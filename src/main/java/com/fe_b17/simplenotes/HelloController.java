package com.fe_b17.simplenotes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    private String hello() {
        return "Hello World!";
    }

}
