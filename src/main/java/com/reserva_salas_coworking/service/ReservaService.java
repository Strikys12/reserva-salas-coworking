package com.reserva_salas_coworking.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reserva_salas_coworking.entity.model.Reserva;
import com.reserva_salas_coworking.entity.model.Sala;
import com.reserva_salas_coworking.entity.model.Usuario;
import com.reserva_salas_coworking.repository.ReservaRepository;
import com.reserva_salas_coworking.repository.SalaRepository;
import com.reserva_salas_coworking.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.DeleteMapping;

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


    @Transactional
    public Reserva save(Reserva reserva) {
        // 1. Validar que los datos básicos existan antes de operar
        if (reserva.getUsuario() == null || reserva.getUsuario().getId() == null) {
            throw new RuntimeException("Es necesario el ID del usuario.");
        }
        if (reserva.getSala() == null || reserva.getSala().getId() == null) {
            throw new RuntimeException("Es necesario el ID de la sala.");
        }
        if (reserva.getHoraInicio() == null || reserva.getHoraFin() == null) {
            throw new RuntimeException("Las horas de inicio y fin son obligatorias.");
        }

        // 2. Buscar entidades en la BD (Aquí es donde confirmamos que existen)
        Usuario usuario = usuarioRepository.findById(reserva.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario con ID " + reserva.getUsuario().getId() + " no encontrado"));

        Sala sala = salaRepository.findById(reserva.getSala().getId())
                .orElseThrow(() -> new RuntimeException("Sala con ID " + reserva.getSala().getId() + " no encontrada"));

        // 3. Cálculos de tiempo
        Duration duracion = Duration.between(reserva.getHoraInicio(), reserva.getHoraFin());
        if (duracion.isNegative() || duracion.isZero()) {
            throw new RuntimeException("La hora de fin debe ser posterior a la hora de inicio.");
        }

        double totalHoras = duracion.toMinutes() / 60.0;

        // 4. Ejecutar validaciones de negocio
        validarFechaAnterior(reserva.getFecha());
        validarPremium(totalHoras, usuario);
        validarDescuento(reserva, totalHoras, usuario, sala);

        reserva.setUsuario(usuario);
        reserva.setSala(sala);

        validarHorario(reserva);

        return reservaRepository.save(reserva);
    }
    public void validarFechaAnterior(LocalDate fecha) {
        if (fecha.isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha de la reserva no puede ser anterior a la fecha actual.");
        }
    }

    public void validarPremium(double totalHoras, Usuario usuario) {
        // Boolean.TRUE.equals devuelve true SOLO si el valor es true.
        // Si es false o es null, devuelve false.
        boolean esPremiumReal = Boolean.TRUE.equals(usuario.getEsPremium());

        if (totalHoras > 4 && !esPremiumReal) {
            throw new RuntimeException("Los usuarios no premium no pueden reservar por más de 4 horas.");
        }
    }


    public void validarDescuento(Reserva reserva, double totalHoras, Usuario usuario, Sala sala) {
        double totalPagar = totalHoras * sala.getPrecio_hora();

        if (usuario.getEsPremium() != null && usuario.getEsPremium()) {
            reserva.setTotalPagar(totalPagar * 0.85);
        } else {
            reserva.setTotalPagar(totalPagar);
        }
    }


    public void validarHorario(Reserva reserva) {
        List<Reserva> reservasExistentes = reservaRepository.findBySalaId(reserva.getSala().getId());
        for (Reserva r : reservasExistentes) {
            // 1. Ignorar la validación si es la MISMA reserva que estamos editando
            if (reserva.getId() != null && reserva.getId().equals(r.getId())) {
                continue;
            }

            if (r.getFecha().equals(reserva.getFecha())) {
                if (reserva.getHoraInicio().isBefore(r.getHoraFin()) && reserva.getHoraFin().isAfter(r.getHoraInicio())) {
                    throw new RuntimeException("Ya existe una reserva en ese horario para esta sala.");
                }
            }
        }
    }

    @Transactional
    public void deleteById(Long id) {
        reservaRepository.deleteById(id);
    }

    //Actualizar
    @Transactional
    public Reserva updateReserva(Long id, Reserva reservaActualizada) {
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La reserva con ID " + id + " no existe."));

        // 1. Volver a buscar Sala y Usuario reales (por si los cambiaron en el JSON)
        Usuario usuario = usuarioRepository.findById(reservaActualizada.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Sala sala = salaRepository.findById(reservaActualizada.getSala().getId())
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        // 2. Recalcular duración y validar
        Duration duracion = Duration.between(reservaActualizada.getHoraInicio(), reservaActualizada.getHoraFin());
        if (duracion.isNegative() || duracion.isZero()) {
            throw new RuntimeException("La hora de fin debe ser posterior a la de inicio.");
        }
        double totalHoras = duracion.toMinutes() / 60.0;

        // 3. Ejecutar de nuevo las validaciones de negocio con los nuevos datos
        validarPremium(totalHoras, usuario);
        validarDescuento(reservaExistente, totalHoras, usuario, sala); // Aquí actualizamos el totalPagar

        // 4. Validar cruce de horarios
        reservaActualizada.setId(id); // Para que validarHorario sepa que es la misma
        validarHorario(reservaActualizada);

        // 5. Actualizar los campos
        reservaExistente.setFecha(reservaActualizada.getFecha());
        reservaExistente.setHoraInicio(reservaActualizada.getHoraInicio());
        reservaExistente.setHoraFin(reservaActualizada.getHoraFin());
        reservaExistente.setUsuario(usuario);
        reservaExistente.setSala(sala);

        return reservaRepository.save(reservaExistente);
    }
}
