package com.fe_b17.simplenotes.user.mapper;

import com.fe_b17.simplenotes.user.dto.UserResponse;
import com.fe_b17.simplenotes.user.models.User;
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
