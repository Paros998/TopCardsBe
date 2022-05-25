package com.cards.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Connector {

    PCI_1_0(name("1.0")),
    PCI_2_0(name("2.0")),
    PCI_3_0(name("3.0")),
    PCI_4_0(name("4.0")),
    PCI_5_0(name("5.0")),

    ;
    private final String type;

    private static String name(String ver) {
        return String.format("PCI Express %s x16", ver);
    }

}
