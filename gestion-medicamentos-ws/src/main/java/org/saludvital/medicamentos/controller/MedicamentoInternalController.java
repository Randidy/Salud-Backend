package org.saludvital.medicamentos.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/medicamentos")
public class MedicamentoInternalController {
    @GetMapping("/{id}")
    public Map<String, Object> byId(@PathVariable Long id) {
        return Map.of("id", id, "nombre", "Medicamento Demo");
    }
}
