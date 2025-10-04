package com.example.lab6_20220229_gtics.repository;

import com.example.lab6_20220229_gtics.entity.NumeroCasa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NumeroCasaRepository extends JpaRepository<NumeroCasa, Long> {
    Optional<NumeroCasa> findFirstByUsuario_IdAndAdivinadoFalse(Long usuarioId);
    List<NumeroCasa> findTop10ByAdivinadoTrueOrderByIntentosAsc();
}
