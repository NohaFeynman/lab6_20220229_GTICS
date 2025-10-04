package com.example.lab6_20220229_gtics.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mesas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer numero;

    @Column(nullable = false)
    private Integer capacidad = 4;

    @Column(nullable = false)
    private Boolean disponible = true;
}
