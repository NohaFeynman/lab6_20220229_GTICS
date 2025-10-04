package com.example.lab6_20220229_gtics.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asignaciones_cancion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AsignacionCancion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne @JoinColumn(name = "cancion_id", nullable = false)
    private Cancion cancion;

    @Column(nullable = false)
    private Integer intentos = 0;

    @Column(nullable = false)
    private Boolean adivinada = false;
}
