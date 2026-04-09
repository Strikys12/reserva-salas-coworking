package com.reserva_salas_coworking.controller;


import com.reserva_salas_coworking.entity.model.Sala;
import com.reserva_salas_coworking.service.SalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salas")

public class SalaController {

    @Autowired
    private SalaService salaService;

    @GetMapping
    public List<Sala> getAll() {
        return salaService.getAllSalas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sala> getById(@PathVariable Long id) {
        Sala sala = salaService.getSalaById(id);
        if (sala != null) {
            return ResponseEntity.ok(sala);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createSala(@RequestBody Sala sala){
        try{
            Sala nuevaSala = salaService.saveSala(sala);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSala);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSala(@PathVariable Long id, @RequestBody Sala sala) {
        try {
            Sala salaActualizada = salaService.updateSala(id, sala);
            return ResponseEntity.ok(salaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSala(@PathVariable Long id) {
        try {
            salaService.deleteSala(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
