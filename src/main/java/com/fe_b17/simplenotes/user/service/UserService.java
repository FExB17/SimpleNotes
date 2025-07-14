package com.fe_b17.simplenotes.user.service;

import com.fe_b17.simplenotes.exception.UserNotFoundException;
import com.fe_b17.simplenotes.user.models.User;
import com.fe_b17.simplenotes.note.repo.NoteRepo;
import com.fe_b17.simplenotes.session.repo.RefreshTokenRepo;
import com.fe_b17.simplenotes.reminder.repo.ReminderRepo;
import com.fe_b17.simplenotes.user.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final NoteRepo noteRepo;
    private final ReminderRepo reminderRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final PasswordEncoder passwordEncoder;

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            throw new UserNotFoundException("No User Found");
        }

        String email = auth.getName();

        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }

    @Transactional
    public void deleteCurrentAccount(String password) {
        User user = getCurrentUser();
        if (!passwordEncoder.matches(user.getPassword(), password)) {
            log.warn("Wrong password: {}",  password);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        noteRepo.deleteByUser(user);
        reminderRepo.deleteByUser(user);
        refreshTokenRepo.deleteByUser(user);
        userRepo.deleteById(getCurrentUser().getId());
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
