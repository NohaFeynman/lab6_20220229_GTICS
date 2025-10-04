package com.example.lab6_20220229_gtics.repository;

import com.example.lab6_20220229_gtics.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
    List<Mesa> findByDisponibleTrue();
    long countByDisponibleTrue();
    long countByDisponibleFalse();
}
