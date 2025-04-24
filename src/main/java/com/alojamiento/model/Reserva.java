package com.alojamiento.model;

import java.time.LocalDate;

public class Reserva {
    private Long id;
    private Long clienteId;
    private Long habitacionId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado; // "CONFIRMADA", "CANCELADA", "PENDIENTE"

    public Reserva() {}

    public Reserva(Long id, Long clienteId, Long habitacionId, LocalDate fechaInicio, LocalDate fechaFin, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.habitacionId = habitacionId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Long getHabitacionId() { return habitacionId; }
    public void setHabitacionId(Long habitacionId) { this.habitacionId = habitacionId; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}