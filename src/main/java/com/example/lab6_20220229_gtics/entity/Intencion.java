package com.example.lab6_20220229_gtics.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "intenciones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Intencion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(name = "fecha")
    private LocalDateTime fecha;
}
