package org.saludvital.cita.client;

import org.saludvital.cita.dto.MedicoDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/internal/medicos")
public interface MedicoClient {
    @GetExchange("/{id}")
    MedicoDto obtenerMedico(@PathVariable Long id);
}
