package com.cards.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class File {
    @Id
    @GeneratedValue (
            strategy = GenerationType.AUTO
    )
    @Column(
            nullable = false,
            updatable = false
    )
    private UUID fileId;
    private String fileName;

    @OneToMany(mappedBy = "avatar",fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<User> users;

    @OneToMany(mappedBy = "cardPhoto",fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<GraphicCard> cards;

}
