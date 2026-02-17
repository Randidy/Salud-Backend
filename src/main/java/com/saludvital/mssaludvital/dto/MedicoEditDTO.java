package com.saludvital.mssaludvital.dto;

import com.saludvital.mssaludvital.enums.Especialidad;
import com.saludvital.mssaludvital.enums.EstadoDoctor;
import java.math.BigDecimal;
import java.util.List;

public class MedicoEditDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email; 
    private String telefono;
    private String numeroLicencia;
    private Especialidad especialidad;
    private BigDecimal tarifaConsulta;
    private EstadoDoctor estado;
    private Boolean disponible;
    private List<HorarioRequest> horarios;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getNumeroLicencia() { return numeroLicencia; }
    public void setNumeroLicencia(String numeroLicencia) { this.numeroLicencia = numeroLicencia; }

    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }

    public BigDecimal getTarifaConsulta() { return tarifaConsulta; }
    public void setTarifaConsulta(BigDecimal tarifaConsulta) { this.tarifaConsulta = tarifaConsulta; }

    public EstadoDoctor getEstado() { return estado; }
    public void setEstado(EstadoDoctor estado) { this.estado = estado; }

    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }

    public List<HorarioRequest> getHorarios() { return horarios; }
    public void setHorarios(List<HorarioRequest> horarios) { this.horarios = horarios; }
}
