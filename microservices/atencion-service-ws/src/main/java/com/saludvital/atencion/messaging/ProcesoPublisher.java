package com.saludvital.atencion.messaging;

import java.time.Instant;
import java.util.Map;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProcesoPublisher {
    private final RabbitTemplate rabbitTemplate;

    public ProcesoPublisher(RabbitTemplate rabbitTemplate) { this.rabbitTemplate = rabbitTemplate; }

    public void publicarProcesoCompletado(Long pacienteId, Long medicoId) {
        Map<String, Object> event = Map.of(
                "eventType", "PROCESO_COMPLETADO",
                "eventVersion", "1.0",
                "occurredAt", Instant.now().toString(),
                "payload", Map.of("pacienteId", pacienteId, "medicoId", medicoId));
        rabbitTemplate.convertAndSend("saludvital.process.exchange", "proceso.completado", event);
    }
}
