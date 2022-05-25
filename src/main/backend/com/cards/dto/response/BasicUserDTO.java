package com.cards.dto.response;

import com.cards.entity.User;
import com.cards.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
public class BasicUserDTO implements Serializable {
    private final UUID userId;
    private final String username;
    private final String email;
    private final Roles role;

    public static BasicUserDTO convertFromEntity(User user) {
        return new BasicUserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}
