package com.fe_b17.simplenotes.mapper;

import com.fe_b17.simplenotes.dto.UserDTO;
import com.fe_b17.simplenotes.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toUserDTO(User user) {
        return new UserDTO(
               user.getId(),
               user.getEmail(),
               user.getUsername(),
               "local"
        );
    }


}
