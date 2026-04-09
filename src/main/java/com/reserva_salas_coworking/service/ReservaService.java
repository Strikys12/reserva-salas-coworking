package com.reserva_salas_coworking.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reserva_salas_coworking.entity.model.Reserva;
import com.reserva_salas_coworking.entity.model.Sala;
import com.reserva_salas_coworking.entity.model.Usuario;
import com.reserva_salas_coworking.repository.ReservaRepository;
import com.reserva_salas_coworking.repository.SalaRepository;
import com.reserva_salas_coworking.repository.UsuarioRepository;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SalaRepository salaRepository;

    //getAll
    public List<Reserva> getAll() {
        return reservaRepository.findAll();
    }

    //getById
    public Reserva getById(Long id) {
        return reservaRepository.findById(id).orElse(null);
    }

    //CreateReserva
    public Reserva save(Reserva reserva) {

        if (reserva.getFecha().isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha de la reserva no puede ser anterior a la fecha actual.");
        }

        Duration duracion = Duration.between(reserva.getHoraInicio(), reserva.getHoraFin());

        long minutos = duracion.toMinutes();
        double totalHoras = minutos / 60.0;

        Usuario usuario = usuarioRepository.findById(reserva.getUsuario().getId()).orElse(null);

        if (totalHoras > 4 && !usuario.esPremium()) {
            throw new RuntimeException("Los usuarios no premium no pueden reservar por más de 4 horas.");
        }

        Sala sala = salaRepository.findById(reserva.getSala().getId()).orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        double totalPagar = (totalHoras * sala.getPrecioHora());

        double descuento = totalPagar * 0.85;

        if (usuario.esPremium()) {
            reserva.setTotalPagar(descuento);
        } else {
            reserva.setTotalPagar(totalPagar);
        }

        return reservaRepository.save(reserva);
    }

}
