package com.cards.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class HistoryModel {
    private final String action;
    private final UUID id;
    private UUID cardId;
    private String link;

    public HistoryModel(String action, UUID id, UUID cardId) {
        this.action = action;
        this.id = id;
        this.cardId = cardId;
    }

    public HistoryModel(String action, UUID id, String link) {
        this.action = action;
        this.id = id;
        this.link = link;
    }
}
