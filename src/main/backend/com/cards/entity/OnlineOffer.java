package com.cards.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "online_offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OnlineOffer {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            nullable = false,
            updatable = false
    )

    private UUID offerId;

    @ManyToOne
    @JoinColumn(name = "card_id")
    @JsonBackReference
    private GraphicCard card;

    private String offerWebsite;

    private Boolean hasFreeShipping;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonBackReference
    private Store store;
}