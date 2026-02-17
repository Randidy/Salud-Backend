package com.saludvital.mssaludvital.dto;

import com.saludvital.mssaludvital.enums.EstadoCita;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class CitaResponse {
    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private String pacienteNombre;
    private String medicoNombre;
    private String especialidad;
    private EstadoCita estado;
    private String motivo;
    private BigDecimal tarifaAplicada;

    public CitaResponse() {}

    public CitaResponse(Long id, LocalDate fecha, LocalTime hora, String pacienteNombre,
                       String medicoNombre, String especialidad, EstadoCita estado,
                       String motivo, BigDecimal tarifaAplicada) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.pacienteNombre = pacienteNombre;
        this.medicoNombre = medicoNombre;
        this.especialidad = especialidad;
        this.estado = estado;
        this.motivo = motivo;
        this.tarifaAplicada = tarifaAplicada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }

    public String getMedicoNombre() {
        return medicoNombre;
    }

    public void setMedicoNombre(String medicoNombre) {
        this.medicoNombre = medicoNombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public BigDecimal getTarifaAplicada() {
        return tarifaAplicada;
    }

    public void setTarifaAplicada(BigDecimal tarifaAplicada) {
        this.tarifaAplicada = tarifaAplicada;
    }
}