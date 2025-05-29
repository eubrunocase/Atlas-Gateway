# Atlas API Gateway

## Visão Geral

O Atlas API Gateway é um microserviço responsável por atuar como gateway de API para a aplicação Atlas, roteando requisições para os microsserviços registrados no Eureka e aplicando regras de segurança e filtros.

## Tecnologias Utilizadas

* **Java 21**
* **Spring Boot 3.4.5**
* **Spring Cloud 2024.0.1**
* **Spring Cloud Gateway**
* **Spring Cloud Netflix Eureka Client**
* **Spring Security WebFlux**
* **Spring Security OAuth2 Resource Server**
* **SpringDoc OpenAPI (Swagger UI)**
* **Maven Wrapper**
* **Docker**
* **Docker Compose**

## Pré-requisitos

* **Java 21+**
* **Maven 3.8+**
* **Docker & Docker Compose** (opcional, para containerização)
* **Servidor Eureka** em execução (ex.: `http://localhost:8761`)
* **Git**

## Instalação e Execução

### Executando localmente

1. Clone o repositório:

   ```bash
   git clone <URL_DO_REPO>
   cd Atlas-API-Gateway
   ```
2. Configure variáveis de ambiente (opcionais):

   ```bash
   export EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka
   export SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_SECRET="OUJWeHM3OUJMPi80SzQ7O0tsQWFNZHtYOmopYWVJVFYK"
   ```
3. Build e execução:

   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

### Usando Docker

1. Build da imagem:

   ```bash
   docker build -t atlas-api-gateway .
   ```
2. Executar o container:

   ```bash
   docker run -d --name atlas-api-gateway \
     -p 8080:8080 \
     -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka \
     -e SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_SECRET="OUJWeHM3OUJMPi80SzQ7O0tsQWFNZHtYOmopYWVJVFYK" \
     atlas-api-gateway
   ```

## Estrutura de Pastas

```text
Atlas-API-Gateway/
├── src/
│   ├── main/
│   │   ├── java/com/atlas/Atlas_API_Gateway
│   │   │   ├── AtlasApiGatewayApplication.java
│   │   │   └── SecurityConfig.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/com/atlas/Atlas_API_Gateway/AtlasApiGatewayApplicationTests.java
├── Dockerfile
├── pom.xml
├── mvnw, mvnw.cmd
└── HELP.md
```

## Configuração

### application.yml

```yaml
server:
  port: 8080

spring:
  application:
    name: atlas-api-gateway
  security:
    oauth2:
      resourceserver:
        jwt:
          secret: "${SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_SECRET:OUJWeHM3OUJMPi80SzQ7O0tsQWFNZHtYOmopYWVJVFYK}"

  cloud:
    gateway:
      routes:
        # Exemplo de rota para Auth-Service
        - id: auth-service
          uri: lb://atlas-auth-service
          predicates:
            - Path=/atlas/auth/**
          filters:
            - StripPrefix=1
        # Defina aqui suas outras rotas

  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: "${EUREKA_CLIENT_SERVICEURL_DEFAULTZONE:http://localhost:8761/eureka}"

logging:
  level:
    org.springframework.security: DEBUG
    org.springframework.cloud.gateway.filter: TRACE
```

## Segurança

A configuração de segurança está em `SecurityConfig.java`, utilizando WebFlux Security com JWT:

* **SecurityWebFilterChain**: define autorização por rota, permitindo ou bloqueando endpoints conforme o token JWT.
* **NimbusReactiveJwtDecoder**: decodifica tokens JWT HMAC SHA256 com o secret configurado.

## Documentação da API

A documentação OpenAPI (Swagger UI) está disponível em:

* `http://localhost:8080/swagger-ui.html`
* `http://localhost:8080/swagger-ui/index.html`

