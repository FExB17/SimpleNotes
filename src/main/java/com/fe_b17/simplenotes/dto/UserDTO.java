package com.fe_b17.simplenotes.dto;

import java.util.UUID;

public record UserDTO(UUID id,
                      String email,
                      String name,
                      String authProvider ) {
}
