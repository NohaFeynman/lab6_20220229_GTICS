package com.example.lab6_20220229_gtics.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "heroes_navales")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Heroe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(length = 50)
    private String pais;
}
