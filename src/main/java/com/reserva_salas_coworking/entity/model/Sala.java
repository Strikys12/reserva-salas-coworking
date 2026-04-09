package com.reserva_salas_coworking.entity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "salas")
@Entity

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nombre", nullable=false, length=30)
    private String nombre;

    @Column(name="capacidad", nullable=false, length=30)
    private String capacidad;

    @Column(name="precioHora", nullable=false, length=10)
    private String precioHora;

}
