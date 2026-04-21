package com.reserva_salas_coworking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reserva_salas_coworking.entity.model.Reserva;
import com.reserva_salas_coworking.service.ReservaService;

@RestController

//ENDPOINT

@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;


    @GetMapping
    public List<Reserva> getAll(){
        return reservaService.getAll();     
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> getById(@PathVariable Long id){
        Reserva reserva = reservaService.getById(id);
        if (reserva != null) {
            return ResponseEntity.ok(reserva);
    }
        return ResponseEntity.notFound().build();
    }



    @PostMapping
    public ResponseEntity<?> createReserva(@RequestBody Reserva reserva) {
        try {
            Reserva nuevaReserva = reservaService.save(reserva);
            // Devolvemos 201 Created porque estamos creando un recurso
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
        } catch (RuntimeException e) {
            // Devolvemos el mensaje de error que definiste en el Service
            // Así el frontend sabe si fue por "Premium", "Fecha" o "Horario"
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReserva(@PathVariable Long id, @RequestBody Reserva reserva) {
        try {
            Reserva reservaActualizada = reservaService.updateReserva(id, reserva);
            return ResponseEntity.ok(reservaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReserva(@PathVariable Long id) {
        try{
            reservaService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


}
