package com.saludvital.reportes.client;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "/api/medicos")
public interface MedicoClient {
    @GetExchange
    String findAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);
}
