package com.saludvital.mssaludvital.dto;

import com.saludvital.mssaludvital.enums.DiaSemana;
import com.saludvital.mssaludvital.enums.Especialidad;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class MedicoRequest {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String numeroLicencia;

    private String telefono;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private Especialidad especialidad;

    @NotNull
    private BigDecimal tarifaConsulta;

    @NotBlank
    private String password;

    private List<HorarioRequest> horarios;


    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getNumeroLicencia() { return numeroLicencia; }
    public void setNumeroLicencia(String numeroLicencia) { this.numeroLicencia = numeroLicencia; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }

    public BigDecimal getTarifaConsulta() { return tarifaConsulta; }
    public void setTarifaConsulta(BigDecimal tarifaConsulta) { this.tarifaConsulta = tarifaConsulta; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<HorarioRequest> getHorarios() { return horarios; }
    public void setHorarios(List<HorarioRequest> horarios) { this.horarios = horarios; }
}