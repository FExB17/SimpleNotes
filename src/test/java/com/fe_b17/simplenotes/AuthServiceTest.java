package com.fe_b17.simplenotes;


import com.fe_b17.simplenotes.auth.dto.RegisterRequest;
import com.fe_b17.simplenotes.auth.service.AuthService;
import com.fe_b17.simplenotes.security.JwtService;
import com.fe_b17.simplenotes.security.PasswordService;
import com.fe_b17.simplenotes.session.repo.RefreshTokenRepo;
import com.fe_b17.simplenotes.session.service.SessionService;
import com.fe_b17.simplenotes.user.repo.UserRepo;
import com.fe_b17.simplenotes.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    UserRepo userRepo;
    @Mock
    PasswordService encoder;
    @Mock
    JwtService jwtService;
    @Mock
    SessionService sessionService;
    @Mock
    UserService userService;
    @Mock
     RefreshTokenRepo refreshTokenRepo;

    AuthService auth;
    MockHttpServletRequest req;





    @Test
    void registerUser_savesNewUser_andReturnsTokens(){
        RegisterRequest dto = new RegisterRequest("test","testEmail@test.de", "12345678","Europe/Berlin");
        when(userRepo.existsByEmail((dto.email()))).thenReturn(false);

        when(encoder.hashPassword("12345678")).thenReturn("password hashed");
    }


}
