package com.saludvital.reportes.controller;

import com.saludvital.reportes.service.ReporteService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {
    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) { this.reporteService = reporteService; }

    @GetMapping("/resumen")
    public String resumen(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return reporteService.resumenGeneral(token);
    }
}
