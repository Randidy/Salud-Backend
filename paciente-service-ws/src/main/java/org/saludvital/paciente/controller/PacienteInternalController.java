package org.saludvital.paciente.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/pacientes")
public class PacienteInternalController {
    @GetMapping("/{id}")
    public Map<String, Object> byId(@PathVariable Long id) {
        return Map.of("id", id, "nombres", "Paciente Demo");
    }
}
