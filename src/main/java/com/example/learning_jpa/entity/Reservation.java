package com.example.learning_jpa.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Vendor vendor;

    @OneToOne
    @JoinColumn(name = "stall_id")
    @JsonIgnore
    private Stall stall;

    private LocalDateTime reservedAt;

    @Column(unique = true)
    private String qrCode;

}
