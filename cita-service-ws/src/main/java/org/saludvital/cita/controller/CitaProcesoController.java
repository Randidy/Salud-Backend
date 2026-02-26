package org.saludvital.cita.controller;

import java.util.Map;
import org.saludvital.cita.dto.CitaCommand;
import org.saludvital.cita.service.CitaOrquestadorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/citas")
public class CitaProcesoController {
    private final CitaOrquestadorService service;
    public CitaProcesoController(CitaOrquestadorService service) { this.service = service; }

    @PostMapping("/proceso")
    public Map<String, Object> procesar(@RequestBody CitaCommand command) {
        return service.crearProceso(command);
    }
}
