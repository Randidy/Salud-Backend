package com.saludvital.mssaludvital.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class RecetaRequest {
    @NotNull
    private Long pacienteId;

    @NotNull
    @Valid
    private List<ItemRecetaRequest> items;

    public RecetaRequest() {}

    public RecetaRequest(Long pacienteId, List<ItemRecetaRequest> items) {
        this.pacienteId = pacienteId;
        this.items = items;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public List<ItemRecetaRequest> getItems() {
        return items;
    }

    public void setItems(List<ItemRecetaRequest> items) {
        this.items = items;
    }

    public static class ItemRecetaRequest {
        @NotNull
        private Long medicamentoId;

        @NotNull
        private String dosis;

        @NotNull
        private String frecuencia;

        public ItemRecetaRequest() {}

        public ItemRecetaRequest(Long medicamentoId, String dosis, String frecuencia) {
            this.medicamentoId = medicamentoId;
            this.dosis = dosis;
            this.frecuencia = frecuencia;
        }

        public Long getMedicamentoId() {
            return medicamentoId;
        }

        public void setMedicamentoId(Long medicamentoId) {
            this.medicamentoId = medicamentoId;
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