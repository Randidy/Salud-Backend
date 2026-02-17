package com.saludvital.mssaludvital.dto;

import java.time.LocalDate;
import java.util.List;

public class RecetaResponse {
    private Long id;
    private LocalDate fechaEmision;
    private LocalDate fechaCaducidad;
    private String pacienteNombre;
    private String medicoNombre;
    private List<ItemRecetaResponse> items;

    public RecetaResponse() {}

    public RecetaResponse(Long id, LocalDate fechaEmision, LocalDate fechaCaducidad,
                         String pacienteNombre, String medicoNombre, List<ItemRecetaResponse> items) {
        this.id = id;
        this.fechaEmision = fechaEmision;
        this.fechaCaducidad = fechaCaducidad;
        this.pacienteNombre = pacienteNombre;
        this.medicoNombre = medicoNombre;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }

    public String getMedicoNombre() {
        return medicoNombre;
    }

    public void setMedicoNombre(String medicoNombre) {
        this.medicoNombre = medicoNombre;
    }

    public List<ItemRecetaResponse> getItems() {
        return items;
    }

    public void setItems(List<ItemRecetaResponse> items) {
        this.items = items;
    }

    public static class ItemRecetaResponse {
        private String medicamentoNombre;
        private String dosis;
        private String frecuencia;

        public ItemRecetaResponse() {}

        public ItemRecetaResponse(String medicamentoNombre, String dosis, String frecuencia) {
            this.medicamentoNombre = medicamentoNombre;
            this.dosis = dosis;
            this.frecuencia = frecuencia;
        }

        public String getMedicamentoNombre() {
            return medicamentoNombre;
        }

        public void setMedicamentoNombre(String medicamentoNombre) {
            this.medicamentoNombre = medicamentoNombre;
        }

        public String getDosis() {
            return dosis;
        }

        public void setDosis(String dosis) {
            this.dosis = dosis;
        }

        public String getFrecuencia() {
            return frecuencia;
        }

        public void setFrecuencia(String frecuencia) {
            this.frecuencia = frecuencia;
        }
    }
}