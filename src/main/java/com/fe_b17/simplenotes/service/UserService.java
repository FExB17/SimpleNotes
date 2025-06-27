package com.fe_b17.simplenotes.service;

import com.fe_b17.simplenotes.models.User;
import com.fe_b17.simplenotes.repo.NoteRepo;
import com.fe_b17.simplenotes.repo.RefreshTokenRepo;
import com.fe_b17.simplenotes.repo.ReminderRepo;
import com.fe_b17.simplenotes.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


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
            throw new RuntimeException("No authenticated user");
        }

        String email = auth.getName();

        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    @Transactional
    public void deleteCurrentAccount(String password) {
        User user = getCurrentUser();
        if (!passwordEncoder.matches(user.getPassword(), password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong Password");
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
