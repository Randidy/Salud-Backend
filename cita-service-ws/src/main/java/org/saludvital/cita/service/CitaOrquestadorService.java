package org.saludvital.cita.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Map;
import org.saludvital.cita.client.MedicamentoClient;
import org.saludvital.cita.client.MedicoClient;
import org.saludvital.cita.client.PacienteClient;
import org.saludvital.cita.dto.CitaCommand;
import org.saludvital.cita.dto.MedicamentoDto;
import org.saludvital.cita.dto.MedicoDto;
import org.saludvital.cita.dto.PacienteDto;
import org.saludvital.cita.messaging.CitaPublisher;
import org.springframework.stereotype.Service;

@Service
public class CitaOrquestadorService {
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;
    private final MedicamentoClient medicamentoClient;
    private final CitaPublisher publisher;

    public CitaOrquestadorService(PacienteClient pacienteClient, MedicoClient medicoClient, MedicamentoClient medicamentoClient, CitaPublisher publisher) {
        this.pacienteClient = pacienteClient;
        this.medicoClient = medicoClient;
        this.medicamentoClient = medicamentoClient;
        this.publisher = publisher;
    }

    @CircuitBreaker(name = "pacienteService", fallbackMethod = "fallbackPaciente")
    public Map<String, Object> crearProceso(CitaCommand cmd) {
        PacienteDto paciente = pacienteClient.obtenerPaciente(cmd.pacienteId());
        MedicoDto medico = medicoClient.obtenerMedico(cmd.medicoId());
        MedicamentoDto medicamento = medicamentoClient.obtenerMedicamento(cmd.medicamentoId());

        Map<String, Object> evento = Map.of(
                "pacienteId", paciente.id(),
                "medicoId", medico.id(),
                "medicamentoId", medicamento.id(),
                "estado", "CONFIRMADA"
        );
        publisher.publicarCitaConfirmada(evento);
        return evento;
    }

    public Map<String, Object> fallbackPaciente(CitaCommand cmd, Throwable t) {
        return Map.of("estado", "PENDIENTE", "motivo", "Servicio de pacientes no disponible");
    }
}
