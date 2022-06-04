package com.cards.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddHistoryDTO {
    private String action;
    private String content;
    private String userId;

    public AddHistoryDTO(String action, String content, String userId) {
        this.action = action;
        this.content = content;
        this.userId = userId;
    }
}
