package com.cards.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSettings {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            nullable = false,
            updatable = false
    )

    private UUID userSettingsId;

    private Boolean isNewCardAdded;

    @Column(name = "available_local")
    private Boolean hasFollowedCardBecomeAvailableLocally;

    @Column(name = "available_online")
    private Boolean hasFollowedCardBecomeAvailableOnline;

    @Column(name = "new_review")
    private Boolean hasFollowedCardNewReview;

    @Column(name = "lower_price_online")
    private Boolean hasFollowedCardLowerOnlinePrice;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public UserSettings(User user) {
        this.user = user;
        this.isNewCardAdded = false;
        this.hasFollowedCardBecomeAvailableLocally = false;
        this.hasFollowedCardBecomeAvailableOnline = false;
        this.hasFollowedCardNewReview = false;
        this.hasFollowedCardLowerOnlinePrice = false;
    }
}