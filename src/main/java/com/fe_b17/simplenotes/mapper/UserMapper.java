package com.fe_b17.simplenotes.mapper;

import com.fe_b17.simplenotes.dto.UserResponse;
import com.fe_b17.simplenotes.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserResponse toResponse(User user) {
        return new UserResponse(
               user.getEmail(),
               user.getUsername(),
               user.getRole().name(),
                user.getCreatedAt()
        );
    }


}
