package com.fe_b17.simplenotes.session.controller;


import com.fe_b17.simplenotes.session.dto.SessionResponse;
import com.fe_b17.simplenotes.session.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/me/sessions")
public class SessionController {
    private final SessionService sessionService;

    @GetMapping()
    public ResponseEntity<List<SessionResponse>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getActiveSessions());
    }

}
