package com.saludvital.mssaludvital.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class CitaRequest {
    @NotNull
    private Long medicoId;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private LocalTime hora;

    private String motivo;

    public CitaRequest() {}

    public CitaRequest(Long medicoId, LocalDate fecha, LocalTime hora, String motivo) {
        this.medicoId = medicoId;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
    }

    public Long getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Long medicoId) {
        this.medicoId = medicoId;
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

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}