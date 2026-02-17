package com.saludvital.mssaludvital.dto;

import java.util.List;

public class MedicoConHorariosResponse {

	 private Long id;
	    private String nombreCompleto;
	    private String especialidad;
	    private List<HorarioResponse> horarios; 

	    public MedicoConHorariosResponse() {}

	    public MedicoConHorariosResponse(Long id, String nombreCompleto, String especialidad, List<HorarioResponse> horarios) {
	        this.id = id;
	        this.nombreCompleto = nombreCompleto;
	        this.especialidad = especialidad;
	        this.horarios = horarios;
	    }

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getNombreCompleto() {
	        return nombreCompleto;
	    }

	    public void setNombreCompleto(String nombreCompleto) {
	        this.nombreCompleto = nombreCompleto;
	    }

	    public String getEspecialidad() {
	        return especialidad;
	    }

	    public void setEspecialidad(String especialidad) {
	        this.especialidad = especialidad;
	    }

	    public List<HorarioResponse> getHorarios() {
	        return horarios;
	    }

	    public void setHorarios(List<HorarioResponse> horarios) {
	        this.horarios = horarios;
	    }
	}