package com.example.lab6_20220229_gtics.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "numeros_casa")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NumeroCasa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "numero_objetivo", nullable = false)
    private Integer numeroObjetivo;

    @Column(nullable = false)
    private Integer intentos = 0;

    @Column(nullable = false)
    private Boolean adivinado = false;
}
