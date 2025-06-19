package com.fe_b17.simplenotes.service;

import com.fe_b17.simplenotes.dto.AuthResponse;
import com.fe_b17.simplenotes.dto.LoginRequest;
import com.fe_b17.simplenotes.dto.RegisterRequest;
import com.fe_b17.simplenotes.exception.EmailAlreadyExistsException;
import com.fe_b17.simplenotes.exception.LoginFailedException;
import com.fe_b17.simplenotes.mapper.UserMapper;
import com.fe_b17.simplenotes.models.User;
import com.fe_b17.simplenotes.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepo userRepo;
    private final PasswordService encoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthResponse registerUser(RegisterRequest dto){
        if (userRepo.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        user.setPassword(encoder.hashPassword(dto.password()));
        userRepo.save(user);

        Map<String, Object> tokenData = jwtService.generateTokenAndExpiration(user);

        return new AuthResponse(
                (String) tokenData.get("token"),
                (Long) tokenData.get("expiresAt"),
                userMapper.toUserDTO(user)
                );
    }

    public AuthResponse login(LoginRequest dto) {
        User user = userRepo.findByEmail(dto.email())
                .orElseThrow(LoginFailedException::new); //um keine emails preiszugeben

        if (!encoder.checkPassword(dto.password(), user.getPassword())){
            throw new LoginFailedException();
        }

        Map<String, Object> tokenData = jwtService.generateTokenAndExpiration(user);

        return new AuthResponse(
                (String) tokenData.get("token"),
                (Long) tokenData.get("expiresAt"),
                userMapper.toUserDTO(user)
        );
    }
}
