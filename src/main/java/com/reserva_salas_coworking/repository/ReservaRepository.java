package com.reserva_salas_coworking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reserva_salas_coworking.entity.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long>{


}
