package com.saludvital.mssaludvital.dto;

import jakarta.validation.constraints.NotBlank;


public class EnfermedadRequest {

	 @NotBlank(message = "El nombre de la enfermedad es obligatorio")
	    private String nombre;

	    private String descripcion;

	    private Long pacienteId; 

	    public String getNombre() {
	        return nombre;
	    }

	    public void setNombre(String nombre) {
	        this.nombre = nombre;
	    }

	    public String getDescripcion() {
	        return descripcion;
	    }

	    public void setDescripcion(String descripcion) {
	        this.descripcion = descripcion;
	    }

	    public Long getPacienteId() {
	        return pacienteId;
	    }

	    public void setPacienteId(Long pacienteId) {
	        this.pacienteId = pacienteId;
	    }
	}
