package org.saludvital.notificaciones.controller;

import java.util.Map;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionesController {

    private final RabbitTemplate rabbitTemplate;

    public NotificacionesController(RabbitTemplate rabbitTemplate) { this.rabbitTemplate = rabbitTemplate; }

    @PostMapping("/publicar")
    public Map<String, String> publicar(@RequestBody Map<String, Object> body) {
        rabbitTemplate.convertAndSend("saludvital.ex", "notificacion.generada", body);
        return Map.of("estado", "PUBLICADO");
    }
}
