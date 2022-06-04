package com.cards.entity;

import com.cards.enums.Action;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class History {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            nullable = false,
            updatable = false
    )
    private UUID historyId;

    @Enumerated(EnumType.STRING)
    private Action action;

    private UUID card;

    private String link;

    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public History(Action action, UUID card, LocalDateTime dateTime, User user) {
        this.action = action;
        this.card = card;
        this.dateTime = dateTime;
        this.user = user;
    }

    public History(Action action, String link, LocalDateTime dateTime, User user) {
        this.action = action;
        this.link = link;
        this.dateTime = dateTime;
        this.user = user;
    }
}