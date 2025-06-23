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

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        Map<String, Object> tokenData = jwtService.generateTokenAndExpiration(user, ip, userAgent,dto.timeZone());

        RefreshToken refreshToken = jwtService.generateRefreshToken(user, tokenData.get("sessionId"));

        return new AuthResponse(
                (String) tokenData.get("token"),
                refreshToken.getId().toString(),
                (Long) tokenData.get("expiresAt"),
                userMapper.toResponse(user)
        );
    }

    public AuthResponse login(LoginRequest dto, HttpServletRequest request) {
        User user = userRepo.findByEmail(dto.email())
                .orElseThrow(LoginFailedException::new);

        if (!encoder.checkPassword(dto.password(), user.getPassword())) {
            throw new LoginFailedException();
        }

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        Map<String, Object> accessTokenData = jwtService.generateTokenAndExpiration(user, ip, userAgent, dto.timeZone());

        RefreshToken refreshTokenData = jwtService.generateRefreshToken(user, accessTokenData.get("sessionId"));

        String refreshTokenId = refreshTokenData.getId().toString();
        String accessToken = (String) accessTokenData.get("token");
        Long expiresAt = (Long) accessTokenData.get("expiresAt");


        return new AuthResponse(
                accessToken,
                refreshTokenId,
                expiresAt,
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

    public RefreshResponse refreshAccessToken(RefreshRequest refreshRequest) {
        UUID tokenUUID = UUID.fromString(refreshRequest.refreshId());

        RefreshToken refreshToken = refreshTokenRepo.findByIdAndActiveTrue(tokenUUID)
                .orElseThrow(() -> new RuntimeException("Invalid or inactive refresh token"));

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            refreshToken.setActive(false);
            refreshTokenRepo.save(refreshToken);
            throw new RuntimeException("Refresh token expired: login to get a new one");
        }

        User user = refreshToken.getUser();
        Session session = refreshToken.getSession();

        refreshToken.setActive(false);
        refreshTokenRepo.save(refreshToken);

        Map<String, Object> tokenData = jwtService.generateTokenAndExpiration(user, session.getId(),refreshRequest.timeZone());

        RefreshToken newRefreshToken = jwtService.generateRefreshToken(user, tokenData.get("sessionId"));

        return new RefreshResponse(
                (String) tokenData.get("token"),
                newRefreshToken.getId().toString(),
                (Long) tokenData.get("expiresAt")
        );

    }

    public void logoutDevice(UUID refreshTokenId) {
        RefreshToken token = refreshTokenRepo.findById(refreshTokenId)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));


        User user = userService.getCurrentUser();
        if(!token.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Unauthorized: Cannot modify foreign token");
        }
        token.setActive(false);
        refreshTokenRepo.save(token);

    }
}
