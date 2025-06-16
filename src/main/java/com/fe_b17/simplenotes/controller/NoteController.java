package com.fe_b17.simplenotes.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoteController {

    @PostMapping("/note")
    public void addNote(){
        // Logic to add a note will go here
    }
}
