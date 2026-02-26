package com.saludvital.atencion.config;

import com.saludvital.atencion.client.MedicoClient;
import com.saludvital.atencion.client.PacienteClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpClientsConfig {

    @Bean
    PacienteClient pacienteClient(@Value("${clients.paciente.url}") String url) {
        RestClient restClient = RestClient.builder().baseUrl(url).build();
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build().createClient(PacienteClient.class);
    }

    @Bean
    MedicoClient medicoClient(@Value("${clients.medico.url}") String url) {
        RestClient restClient = RestClient.builder().baseUrl(url).build();
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build().createClient(MedicoClient.class);
    }
}
