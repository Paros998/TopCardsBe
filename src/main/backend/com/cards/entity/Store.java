package com.cards.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            nullable = false,
            updatable = false
    )

    private UUID storeId;

    private String name;

    @ElementCollection(targetClass=String.class)
    private List<String> addressList;

    private String phone;

    private String website;

    @OneToOne
    @JoinColumn(name = "file_id")
    @JsonIgnore
    private File storePhoto;

    private Integer ratingCount;

    private Float ratingScore;

    @OneToMany(mappedBy = "store")
    @JsonManagedReference
    private List<LocalOffer> localOfferList;

    @OneToMany(mappedBy = "store")
    @JsonManagedReference
    private List<OnlineOffer> onlineOfferList;


}