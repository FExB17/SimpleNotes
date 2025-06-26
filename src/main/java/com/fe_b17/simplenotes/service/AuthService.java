package com.fe_b17.simplenotes.service;

import com.fe_b17.simplenotes.dto.*;
import com.fe_b17.simplenotes.exception.EmailAlreadyExistsException;
import com.fe_b17.simplenotes.exception.LoginFailedException;
import com.fe_b17.simplenotes.mapper.SessionMapper;
import com.fe_b17.simplenotes.mapper.UserMapper;
import com.fe_b17.simplenotes.models.RefreshToken;
import com.fe_b17.simplenotes.models.Session;
import com.fe_b17.simplenotes.models.User;
import com.fe_b17.simplenotes.repo.RefreshTokenRepo;
import com.fe_b17.simplenotes.repo.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    private final RefreshTokenRepo refreshTokenRepo;

    public AuthResponse registerUser(RegisterRequest dto, HttpServletRequest request) {
        if (userRepo.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        user.setPassword(encoder.hashPassword(dto.password()));
        userRepo.save(user);

        return createTokenAndGetAuthResponse(request, user);
    }

    public AuthResponse login(LoginRequest dto, HttpServletRequest request) {
        User user = userRepo.findByEmail(dto.email())
                .orElseThrow(LoginFailedException::new);

        if (!encoder.checkPassword(dto.password(), user.getPassword())) {
            throw new LoginFailedException();
        }

        return createTokenAndGetAuthResponse(request, user);
    }

    private AuthResponse createTokenAndGetAuthResponse(HttpServletRequest request, User user) {
        Map<String, Object> accessTokenData = jwtService.createSessionAndToken(
                user,
                request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                "Europe/Berlin"
        );

        RefreshToken refreshToken = jwtService.generateRefreshToken(user, accessTokenData.get("jti"));

        return new AuthResponse(
                (String) accessTokenData.get("token"),
                refreshToken.getId().toString(),
                (Long) accessTokenData.get("expiresAt"),
                userMapper.toResponse(user)
        );
    }

    public void logout(HttpServletRequest request) {
        String barerToken = extractBarerToken(request);
        UUID sessionId = jwtService.extractSessionId(barerToken);
        Session session = sessionService.deactivateSession(sessionId);
        refreshTokenRepo.findBySession(session).ifPresent(token -> {
            token.setActive(false);
            refreshTokenRepo.save(token);
        });

    }

    public void logoutAll(HttpServletRequest request) {
       extractBarerToken(request);
        User user = userService.getCurrentUser();
        sessionService.deactivateAllSessionsForUser(user);
        refreshTokenRepo.findByUser(user).ifPresent(token -> {
            token.setActive(false);
            refreshTokenRepo.save(token);
        });

    }

    public String extractBarerToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("No Valid Authorization-Header found");
        }
        return authHeader.substring(7);
    }

    public List<SessionResponse> getActiveSessions(HttpServletRequest request) {
        if (request.getHeader("Authorization") == null) {
            throw new IllegalStateException("No Valid Authorization-Header found");
        }
        User user = userService.getCurrentUser();
        return sessionService.getActiveSessions(user)
                .stream()
                .map(SessionMapper::toResponse)
                .toList();
    }

    public RefreshResponse refreshAccessToken(RefreshRequest refreshRequest) {
        UUID tokenUUID = UUID.fromString(refreshRequest.refreshId());

        RefreshToken refreshToken = refreshTokenRepo.findByIdAndActiveTrue(tokenUUID)
                .orElseThrow(() -> new RuntimeException("Invalid or inactive refresh token"));

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            refreshToken.setActive(false);
            refreshTokenRepo.save(refreshToken);
            throw new RuntimeException("Refresh token expired: login to get a new one");
        }

        refreshTokenRepo.delete(refreshToken);


        User user = refreshToken.getUser();
        Session session = refreshToken.getSession();

        if(!session.isActive()) {
            System.out.println("Session is not active");
            return null;
        }

        Map<String, Object> accessTokenData = jwtService.generateAccessTokenForSession(user, session.getId(), "Europe/Berlin");
        RefreshToken newRefreshToken = jwtService.generateRefreshToken(user, session.getId());

        return new RefreshResponse(
                (String) accessTokenData.get("token"),
                newRefreshToken.getId().toString(),
                (Long) accessTokenData.get("expiresAt")
        );
    }

    public void logoutDevice(UUID refreshTokenId) {
        RefreshToken token = refreshTokenRepo.findById(refreshTokenId)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        User user = userService.getCurrentUser();
        if (!token.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized: Cannot modify foreign token");
        }
        token.setActive(false);
        Session session = sessionService.deactivateSession(token.getId());
        token.setActive(false);
        refreshTokenRepo.save(token);
    }
}