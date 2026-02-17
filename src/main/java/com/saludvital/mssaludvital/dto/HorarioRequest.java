package com.saludvital.mssaludvital.dto;

import jakarta.validation.constraints.NotBlank;

public class HorarioRequest {

    @NotBlank
    private String dia; 

    @NotBlank
    private String horaInicio; 

    @NotBlank
    private String horaFin; 

    public String getDia() { return dia; }
    public void setDia(String dia) { this.dia = dia; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }
}
