package com.example.lab6_20220229_gtics.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesa;

    @Column(name = "fecha")
    private LocalDateTime fecha;
}
