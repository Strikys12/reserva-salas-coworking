package com.reserva_salas_coworking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Reserva getById(@PathVariable Long id){
        return reservaService.getById(id);
    }
    
}
