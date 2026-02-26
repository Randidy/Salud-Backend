# SaludVital Microservicios

Proyecto multi-módulo Maven con migración del monolito a microservicios.

## Módulos
- `saludvital-entity`: entidades y enums compartidos.
- `config-server-ws`: configuración centralizada.
- `api-gateway-ws`: entrada única y validación JWT.
- `auth-service-ws`: autenticación y emisión de token JWT.
- `paciente-service-ws`, `medico-service-ws`, `gestion-medicamentos-ws`: CRUD de maestros.
- `atencion-service-ws`: orquestación y publicación `PROCESO_COMPLETADO` en RabbitMQ.
- `reportes-service-ws`: agregación de datos.

## Notas
- Configuración se lee de Config Server (`http://localhost:8888`).
- Cada microservicio expone Actuator con Prometheus.
- Comunicación interna entre proceso/reporte y maestros implementada con `@HttpExchange`.
