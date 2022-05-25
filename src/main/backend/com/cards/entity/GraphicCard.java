package com.cards.entity;

import com.cards.enums.Connector;
import com.cards.enums.Memory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GraphicCard {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            nullable = false,
            updatable = false
    )

    private UUID cardId;

    private String title;

    @ElementCollection(fetch = FetchType.LAZY, targetClass = String.class)
    private List<String> typeOfOutputs;

    private String producentSite;

    private String size;

    private Boolean rtxSupport;

    @ElementCollection(fetch = FetchType.LAZY, targetClass = String.class)
    private List<String> supportedLibraries;

    @Enumerated(EnumType.STRING)
    private Connector typeOfConnector;

    private Integer cudaCoresAmount;

    private Integer powerConsumption;

    private Integer recommendedPower;

    private String cooling;

    private String powerConnector;

    private String coreClock;

    private Integer memoryAmount;

    private Integer memoryClock;

    @Enumerated(EnumType.STRING)
    private Memory typeOfMemory;

    private String producentCode;

    private Integer memoryBus;

    @ManyToOne
    @JoinColumn(name = "file_id")
    @JsonBackReference
    private File cardPhoto;

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Review> reviews;

    @OneToMany(mappedBy = "card")
    @JsonManagedReference
    private List<LocalOffer> localOfferList;

    @OneToMany(mappedBy = "card")
    @JsonManagedReference
    private List<OnlineOffer> onlineOfferList;

}