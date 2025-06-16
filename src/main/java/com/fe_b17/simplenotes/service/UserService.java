package com.fe_b17.simplenotes.service;


import com.fe_b17.simplenotes.dto.LoginRequest;
import com.fe_b17.simplenotes.dto.RegisterRequest;
import com.fe_b17.simplenotes.exception.EmailAlreadyExistsException;
import com.fe_b17.simplenotes.exception.LoginFailedException;
import com.fe_b17.simplenotes.exception.NoSuchAccountException;
import com.fe_b17.simplenotes.models.User;
import com.fe_b17.simplenotes.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordService encoder;

    public void registerUser(RegisterRequest dto){
        if (userRepo.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        user.setPassword(encoder.hashPassword(dto.password()));
        userRepo.save(user);
    }

    public String login(LoginRequest dto) {
        User user = userRepo.findByEmail(dto.email())
                .orElseThrow(NoSuchAccountException::new);

       if (!encoder.checkPassword(dto.password(), user.getPassword())){
            throw new LoginFailedException();
       }

        return "Login successfull " + user.getEmail();
    }
}
