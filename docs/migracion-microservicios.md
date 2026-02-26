# SaludVital migrado a microservicios

## A) Arquitectura final + flujo (Clean Architecture)
- **Entry point**: `api-gateway:8001` (WebFlux + Security JWT + CorrelationId).
- **Discovery/Config**: `eureka-server:8761` y `config-server:8888` (native, `config-repo/`).
- **Servicios de mantenimiento (CRUD + DB)**: `paciente-service-ws:8086`, `medico-service-ws:8082`, `gestion-medicamentos-ws:8081`.
- **Proceso**: `cita-service-ws:8010` orquesta llamadas a CRUD con `@HttpExchange + RestClient + HttpServiceProxyFactory`.
- **Reporte**: `reportes-service-ws:8090` expone agregaciones.
- **Notificaciones**: `notificaciones-service:8089` publisher RabbitMQ.
- **Auth**: `auth-service-ws:8083` emite JWT con roles.

### Flujo principal
1. Angular -> Gateway `/api/auth/login` para token.
2. Angular -> Gateway `/api/...` con Bearer.
3. Gateway valida firma HMAC localmente (no llama a auth-service).
4. Gateway enruta por LB (`lb://...`) y propaga `X-Correlation-Id`.
5. `cita-service-ws` orquesta internamente vía `@HttpExchange` y relaya JWT.
6. `cita-service-ws` publica evento RabbitMQ al finalizar.

## B) Estructura del ecosistema
Ver multi-módulo raíz en `pom.xml`.

## C) Config repo
- `config-repo/application-dev.yml`
- `config-repo/api-gateway-dev.yml`
- `config-repo/auth-service-ws-dev.yml`
- `config-repo/paciente-service-ws-dev.yml`
- `config-repo/medico-service-ws-dev.yml`
- `config-repo/gestion-medicamentos-ws-dev.yml`
- `config-repo/cita-service-ws-dev.yml`
- `config-repo/reportes-service-ws-dev.yml`
- `config-repo/notificaciones-service-dev.yml`
- `config-repo/config-server-dev.yml` (soporte MySQL localhost:3307)

## D) Shared JAR
Módulo `entidades` publica `org.saludvital:entidades:1.0.0`.
Servicios CRUD ya referencian dependencia en sus POM.

## E/F) Implementaciones obligatorias
- Gateway JWT filter: `api-gateway/src/main/java/org/saludvital/gateway/security/JwtValidationFilter.java`.
- Cliente interno con `@HttpExchange`: `cita-service-ws/src/main/java/org/saludvital/cita/client/*`.
- `RestClient + HttpServiceProxyFactory`: `cita-service-ws/src/main/java/org/saludvital/cita/config/HttpClientConfig.java`.
- Propagación JWT + correlation: `cita-service-ws/src/main/java/org/saludvital/cita/config/JwtRelayInterceptor.java`.
- CircuitBreaker: `CitaOrquestadorService#crearProceso`.
- Rabbit publisher: `cita-service-ws/src/main/java/org/saludvital/cita/messaging/CitaPublisher.java`.

## Ejecución local sin Docker
1. Levantar MySQL local `3306` (schema `salud`) y RabbitMQ `5672` credenciales indicadas.
2. `mvn -q -DskipTests install`.
3. Ejecutar en orden: `config-server`, `eureka-server`, `api-gateway`, resto de microservicios.
