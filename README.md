# 🚀 Percentage API

API para el cálculo con porcentaje dinámico y gestión del historial de llamadas. Implementa caché, control de tasa de peticiones y tolerancia a fallos mediante Resilience4j.

## 📌 Descripción

Este servicio permite:
- Calcular la suma de dos números aplicando un porcentaje dinámico obtenido de un servicio externo.
- Registrar y consultar un historial de llamadas a la API.
- Implementar tolerancia a fallos con caché y reintentos automáticos.
- Aplicar un control de tasa para limitar las peticiones por usuario.

## 🛠 Tecnologías usadas

- **Java 21** + **Spring Boot 3.4.3**
- **PostgreSQL 16.3** (Base de datos)
- **Redis 7.4** (Caché)
- **Docker & Docker Compose**
- **Resilience4j** (Circuit Breaker & Rate Limiting)
- **Mockoon** (Simulación de servicio externo)
- **Swagger & OpenAPI** (Documentación de API)
- **Lombok** (Reducir código boilerplate)

## 🚀 Desplegar el servicio localmente

### 1️⃣ **Pre-requisitos**
📌 Asegúrate de tener instalados:
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Git](https://git-scm.com/)
- [Java 21](https://adoptium.net/) y [Maven](https://maven.apache.org/)

---

### 2️⃣ **Clonar el repositorio**
```sh
git clone https://github.com/BlueJnr/percentage-service.git
cd percentage-service
```

---

### 3️⃣ **Ejecutar el servicio con Docker Compose**
```sh
docker-compose up --build
```
📌 **Esto inicia los siguientes servicios:**
- **PostgreSQL** en `localhost:5432`
- **Redis** en `localhost:6379`
- **Mockoon** en `localhost:3000` (Servicio externo simulado)
- **API Percentage** en `localhost:8080/api`

---

### 4️⃣ **Probar los endpoints**
#### **Postman**
📌 Importa la colección de Postman `Backend Tenpo API.postman_collection.json`  
💡 Ejemplo de petición:
```sh
curl -X POST "http://localhost:8080/api/calculate" -H "Content-Type: application/json" -d '{"num1": "5.00", "num2": "10.00"}'
```

📌 **Respuesta esperada:**
```json
{
  "result": "16.50",
  "percentage": "10.00"
}
```

---

## 🏗 **Decisiones técnicas y arquitectura**
### 🔹 **Escalabilidad**
✔️ **Diseñado para múltiples réplicas** → Se usa Redis para caché compartida.  
✔️ **Despliegue con Docker Compose** → Contenedores desacoplados y configurables.  
✔️ **Manejo de concurrencia** → Spring Boot + PostgreSQL + Redis optimizan rendimiento.

### 🔹 **Tolerancia a fallos**
✔️ **Reintentos con Resilience4j** → Hasta 3 intentos antes de usar caché.  
✔️ **Fallback automático** → Si el servicio externo falla, usa el último porcentaje en Redis.

### 🔹 **Rate Limiting**
✔️ **Máximo 3 RPM** → Previene abusos con `@RateLimiter`.  
✔️ **Respuesta HTTP 429** si se excede el límite.

### 🔹 **Documentación**
✔️ **Swagger & OpenAPI** → API-First Development ([tenpo-backend.yaml](src/main/resources/tenpo-backend.yaml)).  
✔️ **Postman Collection** → Pruebas rápidas sin configurar manualmente.

---

## ⚡ **Comandos útiles**
📌 **Levantar servicios:**
```sh
docker-compose up -d
```
📌 **Apagar servicios:**
```sh
docker-compose down
```
📌 **Ver logs:**
```sh
docker-compose logs -f percentage
```
📌 **Reconstruir imagen:**
```sh
docker-compose up --build --force-recreate
```

---

## 📜 **Autor**
👤 **[BlueJnr](https://github.com/BlueJnr)**  
💡 **Tecnologías: Java | Spring Boot | AWS | Kubernetes | CI/CD**  
🚀 **Proyecto mantenido en GitHub → [percentage-service](https://github.com/BlueJnr/percentage-service)**
