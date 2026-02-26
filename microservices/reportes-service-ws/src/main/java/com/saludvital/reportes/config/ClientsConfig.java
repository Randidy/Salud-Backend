package com.saludvital.reportes.config;

import com.saludvital.reportes.client.MedicoClient;
import com.saludvital.reportes.client.PacienteClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientsConfig {
    @Bean
    PacienteClient pacienteClient(@Value("${clients.paciente.url}") String url) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(RestClient.builder().baseUrl(url).build()))
                .build().createClient(PacienteClient.class);
    }

    @Bean
    MedicoClient medicoClient(@Value("${clients.medico.url}") String url) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(RestClient.builder().baseUrl(url).build()))
                .build().createClient(MedicoClient.class);
    }
}
