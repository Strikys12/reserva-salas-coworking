package com.reserva_salas_coworking.service;


import com.reserva_salas_coworking.entity.model.Sala;
import com.reserva_salas_coworking.repository.ReservaRepository;
import com.reserva_salas_coworking.repository.SalaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private ReservaRepository reservaRepository; // <--- Necesario para validar antes de borrar

    public List<Sala> getAllSalas() {
        return salaRepository.findAll();
    }

    public Sala getSalaById(Long id) {
        return salaRepository.findById(id).orElse(null);
    }

    @Transactional
    public Sala saveSala(Sala sala) {
        // 1. Validar que el nombre no esté repetido
        if (salaRepository.existsByNombre(sala.getNombre())) {
            throw new RuntimeException("Ya existe una sala con el nombre: " + sala.getNombre());
        }

        // 2. Validar coherencia de datos
        if (sala.getPrecio_hora() <= 0) {
            throw new RuntimeException("El precio por hora debe ser mayor a cero.");
        }
        if (sala.getCapacidad() <= 0) {
            throw new RuntimeException("La capacidad debe ser de al menos 1 persona.");
        }

        return salaRepository.save(sala);
    }

    @Transactional
    public void deleteSala(Long id) {
        // 1. ¿Existe la sala?
        if (!salaRepository.existsById(id)) {
            throw new RuntimeException("La sala con ID " + id + " no existe.");
        }

        // 2. ¿Tiene reservas? (Usando el boolean que ya creamos en el repo de reservas)
        if (reservaRepository.existsBySalaId(id)) {
            throw new RuntimeException("No se puede eliminar la sala porque tiene reservas programadas.");
        }
        salaRepository.deleteById(id);
    }


    //Actualizar
    @Transactional
    public Sala updateSala(Long id, Sala sala) {
        Sala salaExistente = salaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La sala con ID " + id + " no existe."));

        // Validar que el nuevo nombre no esté repetido (si es diferente al actual)
        if (!salaExistente.getNombre().equals(sala.getNombre()) && salaRepository.existsByNombre(sala.getNombre())) {
            throw new RuntimeException("Ya existe una sala con el nombre: " + sala.getNombre());
        }

        // Validar coherencia de datos
        if (sala.getPrecio_hora() <= 0) {
            throw new RuntimeException("El precio por hora debe ser mayor a cero.");
        }
        if (sala.getCapacidad() <= 0) {
            throw new RuntimeException("La capacidad debe ser de al menos 1 persona.");
        }

        // Actualizar los campos de la sala existente
        salaExistente.setNombre(sala.getNombre());
        salaExistente.setPrecio_hora(sala.getPrecio_hora());
        salaExistente.setCapacidad(sala.getCapacidad());

        return salaRepository.save(salaExistente);
    }
}


