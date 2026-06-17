# Sanos-Salvos-Gateway

API Gateway - enruta peticiones HTTP a los microservicios correspondientes

## Puerto

8080

## Base de datos

Sin base de datos propia - enruta peticiones a microservicios

## Endpoints disponibles

Enruta todas las peticiones /api/* al microservicio correspondiente

## Ejecucion con Docker

docker-compose up --build

## Ejecucion manual

mvn spring-boot:run

## Tecnologias

- Java 21
- Spring Boot 3.2
- Spring Security + JWT
- PostgreSQL
- Docker
