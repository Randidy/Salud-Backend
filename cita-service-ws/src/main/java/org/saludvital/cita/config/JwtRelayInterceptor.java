package org.saludvital.cita.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@Component
public class JwtRelayInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(org.springframework.http.HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest current = attrs.getRequest();
            String auth = current.getHeader(HttpHeaders.AUTHORIZATION);
            if (auth != null) {
                request.getHeaders().set(HttpHeaders.AUTHORIZATION, auth);
            }
            String correlationId = current.getHeader("X-Correlation-Id");
            if (correlationId != null) {
                request.getHeaders().set("X-Correlation-Id", correlationId);
            }
        }
        return execution.execute(request, body);
    }
}
