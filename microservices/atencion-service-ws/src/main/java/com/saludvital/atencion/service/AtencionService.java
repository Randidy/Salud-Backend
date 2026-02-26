package com.saludvital.atencion.service;

import com.saludvital.atencion.client.MedicoClient;
import com.saludvital.atencion.client.PacienteClient;
import com.saludvital.atencion.messaging.ProcesoPublisher;
import org.springframework.stereotype.Service;

@Service
public class AtencionService {
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;
    private final ProcesoPublisher publisher;

    public AtencionService(PacienteClient pacienteClient, MedicoClient medicoClient, ProcesoPublisher publisher) {
        this.pacienteClient = pacienteClient;
        this.medicoClient = medicoClient;
        this.publisher = publisher;
    }

    public String atender(Long pacienteId, Long medicoId, String authHeader) {
        String paciente = pacienteClient.byId(pacienteId, authHeader);
        String medico = medicoClient.byId(medicoId, authHeader);
        publisher.publicarProcesoCompletado(pacienteId, medicoId);
        return "Atención procesada: " + paciente + " / " + medico;
    }
}
