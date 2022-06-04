package com.cards.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class NotificationModelDTO {
    private final UUID cardId;
    private final String message;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private final LocalDateTime date;
    private final String type;
}
