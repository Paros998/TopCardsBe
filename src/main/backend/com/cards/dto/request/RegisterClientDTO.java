package com.cards.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterClientDTO {
    private UUID fileId;
    private String email;
    private String username;
    private String password;
}
