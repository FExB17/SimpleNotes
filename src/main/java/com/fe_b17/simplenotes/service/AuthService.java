package com.fe_b17.simplenotes.service;

import com.fe_b17.simplenotes.dto.*;
import com.fe_b17.simplenotes.exception.EmailAlreadyExistsException;
import com.fe_b17.simplenotes.exception.InvalidTokenException;
import com.fe_b17.simplenotes.exception.LoginFailedException;
import com.fe_b17.simplenotes.exception.UserNotFoundException;
import com.fe_b17.simplenotes.mapper.SessionMapper;
import com.fe_b17.simplenotes.mapper.UserMapper;
import com.fe_b17.simplenotes.models.RefreshToken;
import com.fe_b17.simplenotes.models.Session;
import com.fe_b17.simplenotes.models.User;
import com.fe_b17.simplenotes.repo.RefreshTokenRepo;
import com.fe_b17.simplenotes.repo.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepo userRepo;
    private final PasswordService encoder;
    private final JwtService jwtService;
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
        log.info("New user registered: {}", dto.email());
        return createTokenAndGetAuthResponse(request, user);
    }

    public AuthResponse login(LoginRequest dto, HttpServletRequest request) {
        User user = userRepo.findByEmail(dto.email())
                .orElseThrow(LoginFailedException::new);

        if (!encoder.checkPassword(dto.password(), user.getPassword())) {
            log.warn("Login failed: password mismatch for {}",dto.email());
            throw new LoginFailedException();
        }
        log.info("Login successful for {}", dto.email());
        return createTokenAndGetAuthResponse(request, user);
    }

    private AuthResponse createTokenAndGetAuthResponse(HttpServletRequest request, User user) {
        Session session = sessionService.createSession(user, request.getRemoteAddr(), request.getHeader("User-Agent"));
        Map<String, Object> accessTokenData = jwtService.generateAccessTokenForSession(
                user,
                session.getId(),
                "Europe/Berlin"
        );

        RefreshToken refreshToken = jwtService.generateRefreshToken(user, accessTokenData.get("jti"));

        return new AuthResponse(
                (String) accessTokenData.get("token"),
                refreshToken.getId().toString(),
                (Long) accessTokenData.get("expiresAt"),
                UserMapper.toResponse(user)
        );
    }

    public void logout(HttpServletRequest request) {
        String barerToken = extractBearerToken(request);
        UUID sessionId = jwtService.extractSessionId(barerToken);
        Session session = sessionService.deactivateSession(sessionId);
        refreshTokenRepo.findBySession(session).ifPresent(token -> {
            token.setActive(false);
            refreshTokenRepo.save(token);
        });

    }

    public void logoutAll(HttpServletRequest request) {
       extractBearerToken(request);
        User user = userService.getCurrentUser();
        sessionService.deactivateAllSessionsForUser(user);
        log.info("User {} logged out from all devices", user.getEmail());
    }

    public String extractBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization header missing or malformed");
            throw new InvalidTokenException();
        }
        return authHeader.substring("Bearer ".length());
    }

    public RefreshResponse refreshAccessToken(RefreshRequest refreshRequest) {
        UUID tokenUUID = UUID.fromString(refreshRequest.refreshId());

        RefreshToken refreshToken = refreshTokenRepo.findByIdAndActiveTrue(tokenUUID)
                .orElseThrow(InvalidTokenException::new);

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            refreshToken.setActive(false);
            refreshTokenRepo.save(refreshToken);
            throw new InvalidTokenException();
        }
        refreshTokenRepo.delete(refreshToken);

        User user = refreshToken.getUser();
        Session session = refreshToken.getSession();

        if(!session.isActive()) {
            log.warn("Session has been expired: {}", session.getId());
            throw new InvalidTokenException();
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
                .orElseThrow(InvalidTokenException::new);

        User user = userService.getCurrentUser();
        if (!token.getUser().getId().equals(user.getId())) {
            log.warn("Logout attempt from unauthorized user: {}", token.getUser().getId());
            throw new UserNotFoundException(token.getUser().getEmail());
        }
        token.setActive(false);
        sessionService.deactivateSession(token.getSession().getId());
        token.setActive(false);
        refreshTokenRepo.save(token);
    }
}