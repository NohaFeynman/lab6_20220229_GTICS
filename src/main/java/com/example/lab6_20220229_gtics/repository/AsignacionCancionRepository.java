package com.example.lab6_20220229_gtics.repository;

import com.example.lab6_20220229_gtics.entity.AsignacionCancion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AsignacionCancionRepository extends JpaRepository<AsignacionCancion, Long> {
    Optional<AsignacionCancion> findFirstByUsuario_IdAndAdivinadaFalse(Long usuarioId);
    List<AsignacionCancion> findTop10ByAdivinadaTrueOrderByIntentosAsc();
}
