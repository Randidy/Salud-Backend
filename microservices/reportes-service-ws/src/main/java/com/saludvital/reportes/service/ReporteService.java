package com.saludvital.reportes.service;

import com.saludvital.reportes.client.MedicoClient;
import com.saludvital.reportes.client.PacienteClient;
import org.springframework.stereotype.Service;

@Service
public class ReporteService {
    private final PacienteClient pacientes;
    private final MedicoClient medicos;

    public ReporteService(PacienteClient pacientes, MedicoClient medicos) {
        this.pacientes = pacientes;
        this.medicos = medicos;
    }

    public String resumenGeneral(String token) {
        return "{\"pacientes\":" + pacientes.findAll(token) + ",\"medicos\":" + medicos.findAll(token) + "}";
    }
}
