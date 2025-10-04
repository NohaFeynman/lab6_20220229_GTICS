package com.example.lab6_20220229_gtics.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "canciones_criollas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Cancion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String letra;
}
