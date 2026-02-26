package com.saludvital.mssaludvital.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pacientes", uniqueConstraints = {
        @UniqueConstraint(columnNames = "numero_identificacion")
})
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(length = 100, nullable = false)
    private String nombre;

    @NotBlank
    @Column(length = 100, nullable = false)
    private String apellido;

    @NotBlank
    @Column(name = "numero_identificacion", length = 20, nullable = false, unique = true)
    private String numeroIdentificacion;

    @NotNull
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(length = 20)
    private String telefono;

    @Column(length = 255)
    private String direccion;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User usuario;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @OrderBy("nombre ASC") 
    private Set<Alergia> alergias = new HashSet<>();

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @OrderBy("nombre ASC") 
    private Set<Enfermedad> enfermedades = new HashSet<>();

    public Paciente() {}

    public Paciente(String nombre, String apellido, String numeroIdentificacion, LocalDate fechaNacimiento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.numeroIdentificacion = numeroIdentificacion;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Paciente(String nombre, String apellido, String numeroIdentificacion, LocalDate fechaNacimiento,
                    String telefono, String direccion) {
        this(nombre, apellido, numeroIdentificacion, fechaNacimiento);
        this.telefono = telefono;
        this.direccion = direccion;
    }

    @Transient
    public int getEdad() {
        return (fechaNacimiento == null) ? 0 : Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }

    public Set<Alergia> getAlergias() { return alergias; }
    public void setAlergias(Set<Alergia> alergias) { this.alergias = alergias; }

    public Set<Enfermedad> getEnfermedades() { return enfermedades; }
    public void setEnfermedades(Set<Enfermedad> enfermedades) { this.enfermedades = enfermedades; }
}
