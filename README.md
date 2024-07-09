# Microservices Project

This project involves a microservices architecture designed to manage accounts and customers. It utilizes various technologies and design patterns to ensure the system's scalability, robustness, and maintainability.

## Table of Contents

- [Microservices](#microservices)
    - [API Gateway](#api-gateway)
    - [Account Composite Service](#account-composite-service)
    - [Customer Service](#customer-service)
    - [Event Service](#event-service)
- [Configuration](#configuration)
- [Authentication and Authorization](#authentication-and-authorization)
- [Docker and Kubernetes](#docker-and-kubernetes)
- [Monitoring](#monitoring)
- [Resilience](#resilience)

## Project Structure

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
## Microservices

### API Gateway

The API Gateway acts as the single entry point for all clients. It routes requests to the appropriate microservices and can handle common tasks such as authentication, authorization, and monitoring.

### Account Composite Service

This composite service handles operations related to accounts, aggregating and coordinating data from several underlying microservices:

- `Account`
- `Transaction`
- `TransactionType`

### Customer Service

The customer service manages operations related to the `Customer` entity:

- `Customer`
- `Person`

### Event Service

The event service handles the publishing and processing of events in the system. It facilitates asynchronous communication between microservices.

## Configuration

Service configurations are located in the `config-repo` directory, which contains YAML files for each service:

- `accounts-composite.yml`
- `accounts-service.yml`
- `application.yml`
- `auth-server.yml`
- `customer-service.yml`
- `gateway.yml`

## Authentication and Authorization

The project includes scripts for setting up Auth0 in the `auth0` directory, which allow configuring the tenant and resetting the configuration:

- `env.bash`
- `reset-tenant.bash`
- `setup-tenant.bash`

## Docker and Kubernetes

The project uses Docker for containerization and Kubernetes for deployment and orchestration of the microservices. Relevant configuration files are located in the following directories and files:

- `docker-compose.yml`
- `kubernetes/`

## Monitoring

The setup for monitoring using Grafana and other components is available in `kubernetes/grafana`. It includes dashboards and email notification configurations:

- `Hands-on-Dashboard.json`
- `mail-notification.json`

## Resilience

The project includes configurations for resilience and fault testing, allowing the simulation of delays and errors in the services to ensure system robustness:

- `kubernetes/resilience-tests/`

---

This README provides an overview of the architecture and key components of the project. For more details on how to run and deploy each service, refer to the `HELP.md` files and documentation in each subdirectory.
