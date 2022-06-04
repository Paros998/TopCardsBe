package com.cards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicFilterDTO {
    private final String label;
    private final String value;
}
