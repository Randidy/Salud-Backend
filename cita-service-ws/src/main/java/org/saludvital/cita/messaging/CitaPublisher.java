package org.saludvital.cita.messaging;

import java.util.Map;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class CitaPublisher {
    private final RabbitTemplate rabbitTemplate;

    public CitaPublisher(RabbitTemplate rabbitTemplate) { this.rabbitTemplate = rabbitTemplate; }

    public void publicarCitaConfirmada(Map<String, Object> payload) {
        rabbitTemplate.convertAndSend("saludvital.ex", "cita.confirmada", payload);
    }
}
