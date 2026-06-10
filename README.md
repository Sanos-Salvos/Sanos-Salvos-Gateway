# Sanos y Salvos - Proyecto Completo

API Gateway + Microservicios para la plataforma Sanos y Salvos.

---

## Arquitectura

```
Frontend (React) → Gateway (8080) → BFF (8083) → Microservicios → PostgreSQL
```

### Servicios

| Servicio | Puerto | Base de datos | Descripción |
|---|---|---|---|
| **Gateway** | 8080 | — | Enruta peticiones HTTP a microservicios |
| **Auth** | 8088 | auth_db | Autenticación y JWT |
| **Register** | 8091 | auth_db | Registro de usuarios |
| **BFF** | 8083 | — | Backend For Frontend |
| **Usuarios** | 8087 | usuarios_db | CRUD de usuarios |
| **Organizaciones** | 8081 | organizaciones_db | CRUD de organizaciones |
| **Pet (Mascotas)** | 8082 | mascotas_db | CRUD de mascotas |
| **Coincidencias** | 8084 | coincidencias_db | Matching mascotas-orgs |
| **Geolocalización** | 8085 | geolocalizacion_db | Servicios de ubicación |
| **Notificaciones** | 8086 | notificaciones_db | Envío de notificaciones |
| **Frontend** | 3000 | — | App React |

### Infraestructura

- **PostgreSQL 16** — 7 bases de datos (una por microservicio que necesita BD)
- **Docker Compose** — Orquestación de todos los servicios

---

## Requisitos previos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado y ejecutándose
- Git

---

## Estructura de repositorios

Todos los repos deben estar como carpetas hermanas en el mismo directorio:

```
Sanos-y-Salvos/
├── Sanos-Salvos-Auth/
├── Sanos-Salvos-BFF/
├── Sanos-Salvos-Coincidencias/
├── Sanos-Salvos-Front/
├── Sanos-Salvos-Gateway/          ← Este repo (contiene docker-compose.yml)
├── Sanos-Salvos-Geolocalizacion/
├── Sanos-Salvos-Notificaciones/
├── Sanos-Salvos-Organizaciones/
├── Sanos-Salvos-Pet/
├── Sanos-Salvos-Register/
└── Sanos-Salvos-Usuarios/
```

---

## Cómo ejecutar

### 1. Clonar todos los repos

```bash
git clone https://github.com/TU_ORG/Sanos-Salvos-Auth.git
git clone https://github.com/TU_ORG/Sanos-Salvos-BFF.git
git clone https://github.com/TU_ORG/Sanos-Salvos-Coincidencias.git
git clone https://github.com/TU_ORG/Sanos-Salvos-Front.git
git clone https://github.com/TU_ORG/Sanos-Salvos-Gateway.git
git clone https://github.com/TU_ORG/Sanos-Salvos-Geolocalizacion.git
git clone https://github.com/TU_ORG/Sanos-Salvos-Notificaciones.git
git clone https://github.com/TU_ORG/Sanos-Salvos-Organizaciones.git
git clone https://github.com/TU_ORG/Sanos-Salvos-Pet.git
git clone https://github.com/TU_ORG/Sanos-Salvos-Register.git
git clone https://github.com/TU_ORG/Sanos-Salvos-Usuarios.git
```

### 2. Entrar al directorio del Gateway

```bash
cd Sanos-Salvos-Gateway
```

### 3. Levantar todo con Docker Compose

```bash
docker compose up --build
```

> **Primera vez:** La construcción puede tardar 5-15 minutos dependiendo de tu internet y hardware.

### 4. Verificar que todo funciona

```bash
# Ver los contenedores corriendo
docker compose ps

# Ver logs de un servicio específico
docker compose logs -f auth
docker compose logs -f gateway
```

---

## Orden de ejecución (automático)

Docker Compose maneja el orden automáticamente gracias a `depends_on`:

```
1. PostgreSQL (base de datos)
   └── Espera healthcheck ✅
2. Auth, Register, Usuarios, Organizaciones, Pet, Coincidencias, Geolocalizacion, Notificaciones
   └── Esperan a PostgreSQL ✅
3. Gateway
   └── Espera a todos los microservicios ✅
4. BFF
   └── Espera a todos los microservicios ✅
5. Frontend
   └── Espera a BFF ✅
```

---

## URLs de acceso

| Servicio | URL |
|---|---|
| Frontend | http://localhost:3000 |
| Gateway | http://localhost:8080 |
| BFF | http://localhost:8083 |
| Auth | http://localhost:8088 |
| Register | http://localhost:8091 |
| Usuarios | http://localhost:8087 |
| Organizaciones | http://localhost:8081 |
| Pet | http://localhost:8082 |
| Coincidencias | http://localhost:8084 |
| Geolocalización | http://localhost:8085 |
| Notificaciones | http://localhost:8086 |
| PostgreSQL | localhost:5432 |

---

## Comandos útiles

```bash
# Detener todo
docker compose down

# Detener y borrar volúmenes (resetear BD)
docker compose down -v

# Reconstruir un solo servicio
docker compose up --build auth

# Ver logs en tiempo real
docker compose logs -f

# Entrar a PostgreSQL
docker compose exec postgres psql -U postgres

# Ver estado de los contenedores
docker compose ps
```

---

## Ejecución manual (sin Docker)

Cada microservicio se puede ejecutar individualmente con Maven:

```bash
cd gateway/gateway
./mvnw spring-boot:run
```

**Requisitos manual:** Java 21, PostgreSQL corriendo localmente, Maven.

---

## Tecnologías

- **Backend:** Java 21, Spring Boot 3.2, Spring Security + JWT
- **Frontend:** React
- **Base de datos:** PostgreSQL 16
- **Contenedores:** Docker + Docker Compose
- **Comunicación:** REST (Gateway → BFF → Microservicios)
