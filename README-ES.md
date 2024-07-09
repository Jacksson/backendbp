# Proyecto de Microservicios

Este proyecto consiste en una arquitectura de microservicios diseñada para gestionar cuentas y clientes. Utiliza varias tecnologías y patrones de diseño para asegurar la escalabilidad, robustez y facilidad de mantenimiento del sistema.

## Tabla de Contenidos

- [Microservicios](#microservicios)
    - [API Gateway](#api-gateway)
    - [Account Composite Service](#account-composite-service)
    - [Customer Service](#customer-service)
    - [Event Service](#event-service)
- [Configuración](#configuración)
- [Autenticación y Autorización](#autenticación-y-autorización)
- [Docker y Kubernetes](#docker-y-kubernetes)
- [Monitoreo](#monitoreo)
- [Resiliencia](#resiliencia)

## Estructura del Proyecto

```plaintext
.
├── LICENSE
├── README.md
├── api
│   ├── HELP.md
│   ├── build
│   ├── build.gradle
│   ├── gradle
│   │   └── wrapper
│   │       ├── gradle-wrapper.jar
│   │       └── gradle-wrapper.properties
│   ├── gradlew
│   ├── gradlew.bat
│   ├── settings.gradle
│   └── src
│       └── main
│           └── java
│               └── co
│                   └── jackson
│                       └── microservices
│                           └── api
│                               ├── composite
│                               │   └── account
│                               │       ├── AccountCompositeService.java
│                               │       └── ServiceAddresses.java
│                               ├── core
│                               │   ├── accounts
│                               │   │   ├── Cuenta.java
│                               │   │   ├── CuentaService.java
│                               │   │   ├── Movimiento.java
│                               │   │   ├── MovimientoService.java
│                               │   │   └── TipoMovimiento.java
│                               │   └── customer
│                               │       ├── Cliente.java
│                               │       ├── ClienteService.java
│                               │       └── Persona.java
│                               ├── event
│                               │   └── Event.java
│                               └── exceptions
│                                   ├── BadRequestException.java
│                                   ├── EventProcessingException.java
│                                   ├── InvalidInputException.java
│                                   ├── NotFoundException.java
│                                   └── SaldoInsuficienteException.java
├── auth0
│   ├── env.bash
│   ├── reset-tenant.bash
│   └── setup-tenant.bash
├── config-repo
│   ├── accounts-composite.yml
│   ├── accounts-service.yml
│   ├── application.yml
│   ├── auth-server.yml
│   ├── customer-service.yml
│   └── gateway.yml
├── docker-compose-kafka.yml
├── docker-compose-partitions.yml
├── docker-compose.yml
├── estructura.txt
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── kubernetes
│   ├── efk
│   │   ├── Dockerfile
│   │   ├── fluentd-ds.yml
│   │   ├── fluentd-hands-on-configmap.yml
│   │   └── kibana-api-exported-objects.ndjson
│   ├── first-attempts
│   │   ├── nginx-deployment.yaml
│   │   └── nginx-service.yaml
│   ├── grafana
│   │   └── api-export-import
│   │       ├── Hands-on-Dashboard.json
│   │       └── mail-notification.json
│   ├── hands-on-namespace.yml
│   ├── istio-tracing.yml
│   ├── resilience-tests
│   │   ├── product-virtual-service-with-delay.yml
│   │   └── product-virtual-service-with-faults.yml
│   └── routing-tests
│       └── split-traffic-between-old-and-new-services.bash
├── microservices
│   ├── accounts-composite-service
│   │   ├── Dockerfile
│   │   ├── HELP.md
│   │   ├── build
│   │   ├── build.gradle
│   │   ├── gradle
│   │   │   └── wrapper
│   │   │       ├── gradle-wrapper.jar
│   │   │       └── gradle-wrapper.properties
│   │   ├── gradlew
│   │   ├── gradlew.bat
│   │   ├── settings.gradle
│   │   └── src
│   │       └── main
│   │           └── java
│   │               └── co
│   │                   └── jackson
│   │                       └── microservices
│   │                           └── composite
│   │                               └── accounting
│   │                                   ├── AccountsCompositeServiceApplication.java
│   │                                   ├── OpenApiConfig.java
│   │                                   ├── SecurityConfig.java
│   │                                   └── services
│   │                                       ├── AccountCompositeIntegration.java
│   │                                       └── AccountCompositeServiceImpl.java
│   ├── accounts-service
│   │   ├── Dockerfile
│   │   ├── HELP.md
│   │   ├── build
│   │   ├── build.gradle
│   │   ├── gradle
│   │   │   └── wrapper
│   │   │       ├── gradle-wrapper.jar
│   │   │       └── gradle-wrapper.properties
│   │   ├── gradlew
│   │   ├── gradlew.bat
│   │   ├── settings.gradle
│   │   └── src
│   │       ├── main
│   │       │   └── java
│   │       │       └── co
│   │       │           └── jackson
│   │       │               └── microservices
│   │       │                   └── core
│   │       │                       └── accounts
│   │       │                           ├── AccountsServiceApplication.java
│   │       │                           ├── persistence
│   │       │                           │   ├── CuentaEntity.java
│   │       │                           │   ├── CuentaRepository.java
│   │       │                           │   ├── MovimientoEntity.java
│   │       │                           │   └── MovimientoRepository.java
│   │       │                           └── services
│   │       │                               ├── AccountMessageProcessorConfig.java
│   │       │                               ├── CuentaMapper.java
│   │       │                               ├── CuentaServiceImpl.java
│   │       │                               ├── MovMessageProcessorConfig.java
│   │       │                               ├── MovimientoMapper.java
│   │       │                               └── MovimientoServiceImpl.java
│   │       └── test
│   │           └── java
│   │               └── co
│   │                   └── jackson
│   │                       └── microservices
│   │                           └── core
│   │                               └── accounts
│   └── customer-service
│       ├── Dockerfile
│       ├── HELP.md
│       ├── build
│       ├── build.gradle
│       ├── gradle
│       │   └── wrapper
│       │       ├── gradle-wrapper.jar
│       │       └── gradle-wrapper.properties
│       ├── gradlew
│       ├── gradlew.bat
│       ├── settings.gradle
│       └── src
│           ├── main
│           │   └── java
│           │       └── co
│           │           └── jackson
│           │               └── microservices
│           │                   └── customer
│           │                       ├── CustomerServiceApplication.java
│           │                       ├── persistence
│           │                       │   ├── ClienteEntity.java
│           │                       │   ├── ClienteRepository.java
│           │                       │   ├── PersonaEntity.java
│           │                       │   └── PersonaRepository.java
│           │                       └── services
│           │                           ├── ClienteMapper.java
│           │                           ├── ClienteServiceImpl.java
│           │                           └── MessageProcessorConfig.java
│           └── test
│               └── java
│                   └── co
│                       └── jackson
│                           └── microservices
│                               └── customer
│                                   └── services
│                                       └── ClienteServiceImplTest.java
├── settings.gradle
├── spring-cloud
│   ├── authorization-server
│   │   ├── Dockerfile
│   │   ├── build
│   │   ├── build.gradle
│   │   ├── settings.gradle
│   │   └── src
│   │       ├── main
│   │       │   └── java
│   │       │       └── sample
│   │       │           ├── OAuth2AuthorizationServerApplication.java
│   │       │           ├── config
│   │       │           │   ├── AuthorizationServerConfig.java
│   │       │           │   └── DefaultSecurityConfig.java
│   │       │           └── jose
│   │       │               ├── Jwks.java
│   │       │               └── KeyGeneratorUtils.java
│   │       └── test
│   │           ├── java
│   │           │   └── sample
│   │           │       └── OAuth2AuthorizationServerApplicationTests.java
│   │           └── resources
│   │               └── application.yml
│   └── gateway
│       ├── Dockerfile
│       ├── build
│       ├── build.gradle
│       ├── settings.gradle
│       └── src
│           ├── main
│           │   ├── java
│           │   │   └── se
│           │   │       └── magnus
│           │   │           └── springcloud
│           │   │               └── gateway
│           │   │                   ├── GatewayApplication.java
│           │   │                   ├── HealthCheckConfiguration.java
│           │   │                   └── SecurityConfig.java
│           │   └── resources
│           │       └── keystore
│           │           └── edge.p12
│           └── test
│               ├── java
│               │   └── se
│               │       └── magnus
│               │           └── springcloud
│               │               └── gateway
│               │                   └── GatewayApplicationTests.java
│               └── resources
│                   └── application.yml
└── util
    ├── build
    ├── build.gradle
    ├── gradle
    │   └── wrapper
    │       ├── gradle-wrapper.jar
    │       └── gradle-wrapper.properties
    ├── gradlew
    ├── gradlew.bat
    ├── settings.gradle
    └── src
        └── main
            └── java
                └── co
                    └── jackson
                        └── microservices
                            └── util
                                └── http
                                    ├── GlobalControllerExceptionHandler.java
                                    ├── HttpErrorInfo.java
                                    └── ServiceUtil.java
```

## Microservicios

### API Gateway

El API Gateway actúa como el punto de entrada único para todos los clientes. Se encarga de enrutar las solicitudes a los microservicios apropiados y puede manejar tareas comunes como la autenticación, la autorización y el monitoreo.

### Account Composite Service

Este servicio composite maneja las operaciones relacionadas con las cuentas, agregando y coordinando datos de varios microservicios subyacentes:

- `Cuenta`
- `Movimiento`
- `TipoMovimiento`

### Customer Service

El servicio de clientes gestiona las operaciones relacionadas con la entidad `Cliente`:

- `Cliente`
- `Persona`

### Event Service

El servicio de eventos maneja la publicación y el procesamiento de eventos en el sistema. Facilita la comunicación asíncrona entre microservicios.

## Configuración

La configuración de los servicios se encuentra en el directorio `config-repo`, el cual contiene archivos YAML para cada servicio:

- `accounts-composite.yml`
- `accounts-service.yml`
- `application.yml`
- `auth-server.yml`
- `customer-service.yml`
- `gateway.yml`

## Autenticación y Autorización

El proyecto incluye scripts para la configuración de Auth0 en el directorio `auth0`, los cuales permiten configurar el inquilino y resetear la configuración:

- `env.bash`
- `reset-tenant.bash`
- `setup-tenant.bash`

## Docker y Kubernetes

El proyecto utiliza Docker para la contenedorización y Kubernetes para el despliegue y la orquestación de los microservicios. Los archivos de configuración relevantes se encuentran en los siguientes directorios y archivos:

- `docker-compose.yml`
- `kubernetes/`

## Monitoreo

La configuración para el monitoreo utilizando Grafana y otros componentes está disponible en `kubernetes/grafana`. Incluye dashboards y configuraciones de notificaciones por correo:

- `Hands-on-Dashboard.json`
- `mail-notification.json`

## Resiliencia

El proyecto incluye configuraciones para pruebas de resiliencia y fallos, permitiendo simular retrasos y errores en los servicios para asegurar la robustez del sistema:

- `kubernetes/resilience-tests/`

---

Este README proporciona una visión general de la arquitectura y los componentes clave del proyecto. Para más detalles sobre cómo ejecutar y desplegar cada servicio, consulta los archivos `HELP.md` y la documentación en cada subdirectorio.
