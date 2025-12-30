package com.example.learning_jpa.entity;


import com.example.learning_jpa.enums.StallSize;
import com.example.learning_jpa.enums.StallStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "stalls")
@Getter
@Setter
public class Stall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String stallCode; // A1, B2, C5

    @Enumerated(EnumType.STRING)
    private StallSize size;

    @Enumerated(EnumType.STRING)
    private StallStatus status;

    @OneToOne(mappedBy = "stall")
    @JsonManagedReference
    private Reservation reservation;

    @OneToMany(mappedBy = "stall", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Genre> genres = new HashSet<>();
}
