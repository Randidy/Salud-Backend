package com.saludvital.mssaludvital.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.saludvital.mssaludvital.enums.Especialidad;
import com.saludvital.mssaludvital.enums.EstadoDoctor;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "medicos",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "numeroLicencia")
    }
)
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 100)
    private String nombre;

    @NotBlank
    @Column(length = 100)
    private String apellido;

    @NotBlank
    @Column(unique = true, length = 50)
    private String numeroLicencia;

    @Column(length = 20)
    private String telefono;

    @Email
    @Column(length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Especialidad especialidad;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EstadoDoctor estado = EstadoDoctor.ACTIVO;

    private Boolean disponible = true;

    @DecimalMin(value = "0.0", inclusive = false)
    @Column(precision = 10, scale = 2)
    private BigDecimal tarifaConsulta;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User usuario;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<HorarioAtencion> horarios = new ArrayList<>();

    @OneToMany(mappedBy = "medico", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Cita> citas = new ArrayList<>();

    @OneToMany(mappedBy = "medico", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Receta> recetas = new ArrayList<>();

    public Medico() {}

    public Medico(String nombre, String apellido, String numeroLicencia,
                  Especialidad especialidad, BigDecimal tarifaConsulta) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.numeroLicencia = numeroLicencia;
        this.especialidad = especialidad;
        this.tarifaConsulta = tarifaConsulta;
    }

    @Transient
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public EstadoDoctor getEstado() {
        return estado;
    }

    public void setEstado(EstadoDoctor estado) {
        this.estado = estado;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public BigDecimal getTarifaConsulta() {
        return tarifaConsulta;
    }

    public void setTarifaConsulta(BigDecimal tarifaConsulta) {
        this.tarifaConsulta = tarifaConsulta;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public List<HorarioAtencion> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioAtencion> horarios) {
        this.horarios = horarios;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }
}
