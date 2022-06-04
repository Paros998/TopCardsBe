package com.cards.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum MemoryBus {
    Bit96("96 bit", 96),
    Bit128("128 bit", 128),
    Bit192("192 bit", 192),
    Bit256("256 bit", 256),
    Bit384("384 bit", 384),
    ;
    private final String bus;
    private final Integer memoryBus;

    public static MemoryBus getMemoryBus(String bus) {
        return Arrays.stream(MemoryBus.values())
                .filter(memoryBus -> memoryBus.getBus().equals(bus))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("MemoryBus with bus %s doesn't exists", bus)));
    }
}
