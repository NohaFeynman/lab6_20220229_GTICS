package com.example.lab6_20220229_gtics.repository;

import com.example.lab6_20220229_gtics.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUsuario_Id(Long usuarioId);
    Optional<Reserva> findFirstByUsuario_Id(Long usuarioId);
}
