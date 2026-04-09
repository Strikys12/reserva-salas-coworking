package com.reserva_salas_coworking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reserva_salas_coworking.entity.model.Sala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {
        boolean existsByNombre(String name);

}
