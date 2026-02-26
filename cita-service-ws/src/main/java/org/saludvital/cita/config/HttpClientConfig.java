package org.saludvital.cita.config;

import org.saludvital.cita.client.MedicoClient;
import org.saludvital.cita.client.MedicamentoClient;
import org.saludvital.cita.client.PacienteClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpClientConfig {

    @Bean
    @LoadBalanced
    RestClient.Builder loadBalancedRestClientBuilder(ObjectProvider<ClientHttpRequestInterceptor> jwtRelayInterceptor) {
        RestClient.Builder builder = RestClient.builder();
        jwtRelayInterceptor.ifAvailable(builder::requestInterceptor);
        return builder;
    }

    @Bean
    PacienteClient pacienteClient(@LoadBalanced RestClient.Builder builder) {
        return create(builder, "http://paciente-service-ws");
    }

    @Bean
    MedicoClient medicoClient(@LoadBalanced RestClient.Builder builder) {
        return create(builder, "http://medico-service-ws");
    }

    @Bean
    MedicamentoClient medicamentoClient(@LoadBalanced RestClient.Builder builder) {
        return create(builder, "http://gestion-medicamentos-ws");
    }

    private <T> T create(RestClient.Builder builder, String baseUrl) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(builder.baseUrl(baseUrl).build())).build();
        return (T) factory.createClient(switch (baseUrl) {
            case "http://paciente-service-ws" -> PacienteClient.class;
            case "http://medico-service-ws" -> MedicoClient.class;
            default -> MedicamentoClient.class;
        });
    }
}
