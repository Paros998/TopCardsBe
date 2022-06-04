package com.cards.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum MemoryAmount {
    GB2("2GB", 2),
    GB3("3GB", 3),
    GB4("4GB", 4),
    GB6("6GB", 6),
    GB8("8GB", 8),
    GB12("12GB", 12),
    GB16("16GB", 16),
    GB24("24GB", 24),
    ;
    private final String amount;
    private final Integer memoryAmount;

    public static MemoryAmount getMemoryAmount(String amount) {
        return Arrays.stream(MemoryAmount.values())
                .filter(memoryAmount -> memoryAmount.getAmount().equals(amount))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Enum with given %s amount doesn't exists", amount)));
    }

}
