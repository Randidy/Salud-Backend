package com.saludvital.mssaludvital.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class SignUpRequest {

    @NotBlank
    @Size(max = 100)
    private String nombre;  

    @NotBlank
    @Size(max = 100)
    private String apellido;

    @NotBlank
    @Size(max = 100)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(max = 20)
    private String numeroIdentificacion;

    @NotNull
    private LocalDate fechaNacimiento;

    private String telefono;
    private String direccion;

    private List<String> alergias;
    private List<String> enfermedades;

    public SignUpRequest(String nombre, String apellido, String email, String password,
                         String numeroIdentificacion, LocalDate fechaNacimiento,
                         String telefono, String direccion,
                         List<String> alergias, List<String> enfermedades) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.numeroIdentificacion = numeroIdentificacion;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.direccion = direccion;
        this.alergias = alergias;
        this.enfermedades = enfermedades;
    }

    public SignUpRequest() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public List<String> getAlergias() { return alergias; }
    public void setAlergias(List<String> alergias) { this.alergias = alergias; }

    public List<String> getEnfermedades() { return enfermedades; }
    public void setEnfermedades(List<String> enfermedades) { this.enfermedades = enfermedades; }
}
