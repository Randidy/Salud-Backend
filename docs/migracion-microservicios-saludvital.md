# SaludVital migrado a arquitectura de microservicios

Esta propuesta deja un **workspace real de microservicios** en `microservices/` listo para evolucionar en local sin Docker, cumpliendo Spring Cloud, JWT en Gateway y comunicación interna con `@HttpExchange`.

## Estructura implementada

```text
microservices/
├── pom.xml (agregador)
├── saludvital-entity/                 # JAR compartido: entidades/enums JPA
├── config-server-ws/                  # Spring Cloud Config Server
├── api-gateway-ws/                    # Entrada única Angular + validación JWT local
├── auth-service-ws/                   # Login contra MySQL + emisión JWT
├── paciente-service-ws/               # CRUD Paciente
├── medico-service-ws/                 # CRUD Medico
├── gestion-medicamentos-ws/           # CRUD Medicamento
├── atencion-service-ws/               # Orquestación + publisher RabbitMQ
└── reportes-service-ws/               # Agregación con @HttpExchange
```

## Decisiones de arquitectura aplicadas

- **Clean Architecture por servicio**: controladores (interface), servicios de uso (application), repos/clientes/adapters (infrastructure).
- **Shared Kernel**: las entidades y enums se movieron a `saludvital-entity` y se consumen como dependencia Maven.
- **Seguridad**:
  - `auth-service-ws` emite JWT firmado HMAC.
  - `api-gateway-ws` valida JWT localmente con clave compartida (sin pedir validación al auth).
- **Comunicación interna obligatoria**: `atencion-service-ws` y `reportes-service-ws` consumen servicios vía `@HttpExchange + RestClient + HttpServiceProxyFactory`.
- **Mensajería táctica**: `atencion-service-ws` publica `PROCESO_COMPLETADO` a RabbitMQ (sin consumers, como se pidió).
- **Observabilidad**: todos los servicios con Actuator y endpoint Prometheus expuesto.
- **Config externalizada**: todos los servicios usan `spring.config.import=optional:configserver:http://localhost:8888`.

## Puertos locales sugeridos

- Config Server: 8888
- Gateway: 8080
- Auth: 8081
- Paciente: 8091
- Medico: 8092
- Medicamentos: 8093
- Atencion: 8094
- Reportes: 8095

## Arranque recomendado

1. Levantar `config-server-ws`.
2. Levantar `auth-service-ws` + servicios de mantenimiento.
3. Levantar `atencion-service-ws` y `reportes-service-ws`.
4. Levantar `api-gateway-ws`.
