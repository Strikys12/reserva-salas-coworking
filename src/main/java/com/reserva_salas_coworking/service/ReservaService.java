package com.reserva_salas_coworking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reserva_salas_coworking.entity.model.Reserva;
import com.reserva_salas_coworking.repository.ReservaRepository;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    //getAll
    public List<Reserva> getAll(){
        return reservaRepository.findAll();    
    }


    //getById

    public Reserva getById(Long id){
        return reservaRepository.findById(id).orElse(null);
    }


    //CreateReserva

    public Reserva save(Reserva reserva){
        return reservaRepository.save(reserva);
    }



}
