package com.saludvital.mssaludvital.entity;

import com.saludvital.mssaludvital.enums.EstadoCita;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "citas")
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private LocalTime hora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    @NotNull
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    @NotNull
    private Medico medico;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EstadoCita estado = EstadoCita.PROGRAMADA;

    @Column(length = 500)
    private String motivo;

    @DecimalMin(value = "0.0", inclusive = false)
    @Column(precision = 10, scale = 2)
    private BigDecimal tarifaAplicada;

    public Cita() {}

    public Cita(LocalDate fecha, LocalTime hora, Paciente paciente, 
                Medico medico, String motivo, BigDecimal tarifaAplicada) {
        this.fecha = fecha;
        this.hora = hora;
        this.paciente = paciente;
        this.medico = medico;
        this.motivo = motivo;
        this.tarifaAplicada = tarifaAplicada;
    }

    // Getters and Setters
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

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
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