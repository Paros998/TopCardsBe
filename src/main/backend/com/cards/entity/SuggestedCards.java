package com.cards.entity;

import lombok.*;

import javax.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "suggested")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuggestedCards{

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            nullable = false,
            updatable = false
    )
    private UUID cardId;

}