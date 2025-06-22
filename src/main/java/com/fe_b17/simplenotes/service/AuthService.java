package com.fe_b17.simplenotes.service;

import com.fe_b17.simplenotes.dto.AuthResponse;
import com.fe_b17.simplenotes.dto.LoginRequest;
import com.fe_b17.simplenotes.dto.RegisterRequest;
import com.fe_b17.simplenotes.dto.SessionResponse;
import com.fe_b17.simplenotes.exception.EmailAlreadyExistsException;
import com.fe_b17.simplenotes.exception.LoginFailedException;
import com.fe_b17.simplenotes.mapper.SessionMapper;
import com.fe_b17.simplenotes.mapper.UserMapper;
import com.fe_b17.simplenotes.models.User;
import com.fe_b17.simplenotes.repo.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepo userRepo;
    private final PasswordService encoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final SessionService sessionService;
    private final UserService userService;

    public AuthResponse registerUser(RegisterRequest dto,  HttpServletRequest request) {
        if (userRepo.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        user.setPassword(encoder.hashPassword(dto.password()));
        userRepo.save(user);

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        Map<String, Object> tokenData = jwtService.generateTokenAndExpiration(user, ip, userAgent);

        return new AuthResponse(
                (String) tokenData.get("token"),
                (Long) tokenData.get("expiresAt"),
                userMapper.toResponse(user)
                );
    }

    public AuthResponse login(LoginRequest dto, HttpServletRequest request) {
        User user = userRepo.findByEmail(dto.email())
                .orElseThrow(LoginFailedException::new); //um keine emails preiszugeben

        if (!encoder.checkPassword(dto.password(), user.getPassword())){
            throw new LoginFailedException();
        }

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        Map<String, Object> tokenData = jwtService.generateTokenAndExpiration(user, ip, userAgent);

        return new AuthResponse(
                (String) tokenData.get("token"),
                (Long) tokenData.get("expiresAt"),
                userMapper.toResponse(user)
        );
    }

    public void logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("No Valid Authorization-Header found");
        }

        String authToken = authHeader.substring(7);
        UUID sessionId = jwtService.extractSessionId(authToken);
        sessionService.deactivateSession(sessionId);

    }

    public void logoutAll(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("No Valid Authorization-Header found");
        }
        User user = userService.getCurrentUser();
        sessionService.deactivateAllSessionsForUser(user);

    }

    public List<SessionResponse> getActiveSessions(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("No Valid Authorization-Header found");
        }
        User user = userService.getCurrentUser();
        return sessionService.getActiveSessions(user)
                .stream()
                .map(SessionMapper::toResponse)
                .toList();

    }
}
