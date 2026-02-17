package com.saludvital.mssaludvital.dto;

import java.util.List;

public class ExpedienteResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String numeroIdentificacion;
    private String fechaNacimiento;
    private int edad;
    private String telefono;
    private String direccion;

    private List<String> alergias;
    private List<String> enfermedades;
    private List<CitaResponse> citas;
    private List<RecetaResponse> recetas;

    public ExpedienteResponse() {}

    
    public ExpedienteResponse(Long id, String nombre, String apellido,
                              String numeroIdentificacion, String fechaNacimiento, int edad,
                              String telefono, String direccion,
                              List<String> alergias, List<String> enfermedades,
                              List<CitaResponse> citas, List<RecetaResponse> recetas) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.numeroIdentificacion = numeroIdentificacion;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.telefono = telefono;
        this.direccion = direccion;
        this.alergias = alergias;
        this.enfermedades = enfermedades;
        this.citas = citas;
        this.recetas = recetas;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(String numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public List<String> getAlergias() { return alergias; }
    public void setAlergias(List<String> alergias) { this.alergias = alergias; }

    public List<String> getEnfermedades() { return enfermedades; }
    public void setEnfermedades(List<String> enfermedades) { this.enfermedades = enfermedades; }

    public List<CitaResponse> getCitas() { return citas; }
    public void setCitas(List<CitaResponse> citas) { this.citas = citas; }

    public List<RecetaResponse> getRecetas() { return recetas; }
    public void setRecetas(List<RecetaResponse> recetas) { this.recetas = recetas; }
}
