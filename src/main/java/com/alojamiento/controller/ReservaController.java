package com.alojamiento.controller;

import com.alojamiento.model.Reserva;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);
    private final List<Reserva> reservas = new ArrayList<>();

    // ---- ENDPOINTS ----

    // POST /reservas - Crear reserva
    @PostMapping
    public ResponseEntity<Reserva> crearReserva(@RequestBody Reserva nuevaReserva) {
        try {
            // Validaciones básicas
            if (nuevaReserva.getClienteId() == null || nuevaReserva.getHabitacionId() == null) {
                throw new IllegalArgumentException("Datos incompletos");
            }

            // Validar fechas
            if (nuevaReserva.getFechaInicio() == null || nuevaReserva.getFechaFin() == null ||
                    nuevaReserva.getFechaFin().isBefore(nuevaReserva.getFechaInicio())) {
                throw new IllegalArgumentException("Fechas inválidas");
            }

            // Validar solapamiento (opcional)
            boolean existeSolapamiento = reservas.stream()
                    .anyMatch(r -> r.getHabitacionId().equals(nuevaReserva.getHabitacionId()) &&
                            !r.getFechaFin().isBefore(nuevaReserva.getFechaInicio()) &&
                            !nuevaReserva.getFechaFin().isBefore(r.getFechaInicio()));

            if (existeSolapamiento) {
                throw new IllegalStateException("Habitación no disponible en esas fechas");
            }

            reservas.add(nuevaReserva);
            logger.info("Reserva creada: {}", nuevaReserva);
            return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Error al crear reserva: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // GET /reservas - Listar todas
    @GetMapping
    public ResponseEntity<List<Reserva>> listarReservas() {
        logger.info("Consultando todas las reservas");
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }

    // GET /reservas/{id} - Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerReserva(@PathVariable Long id) {
        Optional<Reserva> reserva = reservas.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();

        return reserva.map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // PUT /reservas/{id} - Actualización completa
    @PutMapping("/{id}")
    public ResponseEntity<Reserva> actualizarReserva(
            @PathVariable Long id,
            @RequestBody Reserva reservaActualizada) {

        Optional<Reserva> reservaExistente = reservas.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();

        if (reservaExistente.isPresent()) {
            Reserva reserva = reservaExistente.get();
            reserva.setClienteId(reservaActualizada.getClienteId());
            reserva.setHabitacionId(reservaActualizada.getHabitacionId());
            reserva.setFechaInicio(reservaActualizada.getFechaInicio());
            reserva.setFechaFin(reservaActualizada.getFechaFin());
            reserva.setEstado(reservaActualizada.getEstado());

            logger.info("Reserva actualizada: ID {}", id);
            return new ResponseEntity<>(reserva, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // PATCH /reservas/{id}/estado - Actualizar estado
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Reserva> actualizarEstado(
            @PathVariable Long id,
            @RequestBody String nuevoEstado) {

        Optional<Reserva> reserva = reservas.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();

        if (reserva.isPresent()) {
            reserva.get().setEstado(nuevoEstado);
            logger.info("Estado actualizado: ID {} -> {}", id, nuevoEstado);
            return new ResponseEntity<>(reserva.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DELETE /reservas/{id} - Eliminar reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarReserva(@PathVariable Long id) {
        boolean eliminada = reservas.removeIf(r -> r.getId().equals(id));
        if (eliminada) {
            logger.info("Reserva eliminada: ID {}", id);
            return new ResponseEntity<>("Reserva eliminada", HttpStatus.OK);
        }
        return new ResponseEntity<>("Reserva no encontrada", HttpStatus.NOT_FOUND);
    }
}