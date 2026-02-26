package org.saludvital.cita.client;

import org.saludvital.cita.dto.PacienteDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/internal/pacientes")
public interface PacienteClient {
    @GetExchange("/{id}")
    PacienteDto obtenerPaciente(@PathVariable Long id);
}
