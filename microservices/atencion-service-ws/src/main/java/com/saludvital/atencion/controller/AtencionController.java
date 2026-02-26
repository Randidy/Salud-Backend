package com.saludvital.atencion.controller;

import com.saludvital.atencion.service.AtencionService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/atenciones")
public class AtencionController {

    private final AtencionService service;

    public AtencionController(AtencionService service) { this.service = service; }

    @GetMapping("/procesar")
    public String procesar(@RequestParam Long pacienteId,
                           @RequestParam Long medicoId,
                           @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        return service.atender(pacienteId, medicoId, authHeader);
    }
}
