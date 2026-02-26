package org.saludvital.cita.client;

import org.saludvital.cita.dto.MedicamentoDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/internal/medicamentos")
public interface MedicamentoClient {
    @GetExchange("/{id}")
    MedicamentoDto obtenerMedicamento(@PathVariable Long id);
}
