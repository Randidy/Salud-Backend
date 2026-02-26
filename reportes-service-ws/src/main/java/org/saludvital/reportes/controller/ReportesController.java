package org.saludvital.reportes.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {
    @GetMapping("/resumen")
    public Map<String, Object> resumen() {
        return Map.of("citasHoy", 12, "medicosActivos", 5);
    }
}
