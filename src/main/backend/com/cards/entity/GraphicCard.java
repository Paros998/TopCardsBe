package com.cards.entity;

import com.cards.enums.*;
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

    @Enumerated(EnumType.STRING)
    private Technology technology;

    @ElementCollection(fetch = FetchType.LAZY, targetClass = String.class)
    private List<String> typeOfOutputs;

    @Column(
            length = 2000
    )
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

    @Enumerated(EnumType.STRING)
    private MemoryAmount memoryAmount;

    @Enumerated(EnumType.STRING)
    private Manufacturer manufacturer;

    private Integer memoryClock;

    @Enumerated(EnumType.STRING)
    private MemoryType typeOfMemory;

    private String producentCode;

    @Enumerated(EnumType.STRING)
    private MemoryBus memoryBus;

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

    public GraphicCard(String title,
                       Technology technology,
                       List<String> typeOfOutputs,
                       String producentSite,
                       String size,
                       Boolean rtxSupport,
                       List<String> supportedLibraries,
                       Connector typeOfConnector,
                       Integer cudaCoresAmount,
                       Integer powerConsumption,
                       Integer recommendedPower,
                       String cooling,
                       String powerConnector,
                       String coreClock,
                       MemoryAmount memoryAmount,
                       Manufacturer manufacturer,
                       Integer memoryClock,
                       MemoryType typeOfMemory,
                       String producentCode,
                       MemoryBus memoryBus,
                       File cardPhoto) {
        this.title = title;
        this.technology = technology;
        this.typeOfOutputs = typeOfOutputs;
        this.producentSite = producentSite;
        this.size = size;
        this.rtxSupport = rtxSupport;
        this.supportedLibraries = supportedLibraries;
        this.typeOfConnector = typeOfConnector;
        this.cudaCoresAmount = cudaCoresAmount;
        this.powerConsumption = powerConsumption;
        this.recommendedPower = recommendedPower;
        this.cooling = cooling;
        this.powerConnector = powerConnector;
        this.coreClock = coreClock;
        this.memoryAmount = memoryAmount;
        this.manufacturer = manufacturer;
        this.memoryClock = memoryClock;
        this.typeOfMemory = typeOfMemory;
        this.producentCode = producentCode;
        this.memoryBus = memoryBus;
        this.cardPhoto = cardPhoto;
    }
}