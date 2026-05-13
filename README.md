# Sanos-Salvos-Gateway
Microservicio **API Gateway** encargado de gestionar el enrutamiento y la validación de seguridad JWT dentro del ecosistema *Sanos y Salvos*.

---

# Requisitos

- Java JDK 17
- Maven 3.8+
- IntelliJ IDEA (opcional)
- Postman o Insomnia

---

# Instalación

## Clonar repositorio

```bash
git clone https://github.com/TU_USUARIO/Sanos-Salvos-Gateway.git
cd Sanos-Salvos-Gateway
```

## Instalar dependencias

### Linux / Mac

```bash
mvn clean install
```

### Windows

```powershell
.\mvnw clean install
```

---

# Configuración

Verificar:

```bash
src/main/resources/application.properties
```

Configuración principal:

```properties
server.port=8080
spring.application.name=api-gateway

jwt.secret=sanosysalvos1234

spring.cloud.gateway.routes[0].id=sanosysalvos-bff
spring.cloud.gateway.routes[0].uri=http://localhost:8083
spring.cloud.gateway.routes[0].predicates[0]=Path=/api/bff/**
spring.cloud.gateway.routes[0].filters[0]=JwtAuthenticationFilter
spring.cloud.gateway.routes[0].filters[1]=Name=CircuitBreaker,args[name]=bffCircuitBreaker,args[fallbackUri]=forward:/fallback/bff

spring.cloud.gateway.routes[1].id=login-service
spring.cloud.gateway.routes[1].uri=http://localhost:8089
spring.cloud.gateway.routes[1].predicates[0]=Path=/api/login/**

spring.cloud.gateway.routes[2].id=auth-service
spring.cloud.gateway.routes[2].uri=http://localhost:8088
spring.cloud.gateway.routes[2].predicates[0]=Path=/api/auth/**

spring.cloud.gateway.globalcors.cors-configurations.[/**].allowedOrigins=http://localhost:3000
spring.cloud.gateway.globalcors.cors-configurations.[/**].allowedMethods=GET,POST,PUT,DELETE,OPTIONS
spring.cloud.gateway.globalcors.cors-configurations.[/**].allowedHeaders=*

spring.cloud.gateway.discovery.locator.enabled=true
```

---

# Ejecución

## Desde IntelliJ

Ejecutar:

```bash
GatewayApplication.java
```

## Desde consola

```bash
mvn spring-boot:run
```

El Gateway quedará disponible en:

```bash
http://localhost:8080
```

---

# Pruebas API

## Login (Ruta Pública)

### POST

```http
POST http://localhost:8080/api/login/ingresar
```

### Body

```json
{
  "username": "usuario@sanosysalvos.com",
  "password": "password123"
}
```

### Respuesta esperada

```json
{
  "token": "eyJhbGciOiJIUzI1Ni..."
}
```

---

# Ruta Protegida hacia el BFF

El Gateway valida el JWT antes de redirigir la solicitud.

## GET

```http
GET http://localhost:8080/api/bff/main/dashboard?usuarioId=1&organizacionId=1
```

## Header requerido

```http
Authorization: Bearer TU_TOKEN_JWT
```

---

# Arquitectura

```text
Frontend (React :3000)
        ↓
API Gateway (:8080)
        ↓
BFF (:8083)
        ↓
Microservicios internos
```

Tecnologías utilizadas:

- Spring Boot
- Spring Cloud Gateway
- JWT Authentication
- Reverse Proxy Pattern
- Gateway Filter Pattern