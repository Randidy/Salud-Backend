package com.saludvital.atencion.client;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "/api/medicos")
public interface MedicoClient {
    @GetExchange("/{id}")
    String byId(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken);
}
