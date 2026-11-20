# Backend API REST Persistente — Tienda Sonido Vivo 🎧

**Institución:** Duoc UC  
**Asignatura:** DSY1104 — Desarrollo Full Stack II  
**Evaluación:** Evaluación Parcial 3 (EP3) — Integración y Persistencia Full Stack  
**Entorno Técnico:** Java 21 LTS · Spring Boot 3.3.5 · Spring Data JPA · MySQL 8.x / MariaDB (XAMPP Puerto 3307) · Spring Security 6 · JJWT 0.12.6 · Swagger / OpenAPI 3 · Maven Wrapper  

---

## 🔗 Repositorio del Frontend React

El frontend correspondiente a esta API REST se encuentra alojado en su propio repositorio:  
👉 **[Repositorio Frontend React — Tienda-Sonido-Vivo](https://github.com/BenGomezDUOC/Tienda-Sonido-Vivo)**

---

## 🏛️ Arquitectura del Sistema

El backend implementa una arquitectura en capas desacoplada con seguridad sin estado basada en JWT y persistencia relacional en MySQL:

```
[ Cliente HTTP / Swagger UI / React :5173 ]
                   │
                   ▼ (HTTP / JSON + Header 'Authorization: Bearer <token>')
           [ JwtAuthFilter ] ─── Valida firma y expiración del JWT
                   │
                   ▼
     [@RestController: AuthController / ProductoController]
                   │
                   ▼
       [@Service: AuthService / ProductoService]
                   │
                   ▼
  [@Repository: UsuarioRepository / ProductoRepository]
                   │
                   ▼ (Spring Data JPA / Hibernate)
      [ MySQL en XAMPP (Puerto 3307): sonidovivo_db ]
```

### Componentes Principales:
1. **Entidades JPA (`cl.duoc.sonidovivo.model`)**:
   - `Producto`: id, nombre, precio, categoria, imagenUrl, descripcion, stock con validaciones `@NotBlank`, `@Min(1)`.
   - `Usuario`: id, nombre, email único, password encriptada con BCrypt (`@JsonIgnore`), rol (`ROLE_ADMIN`, `ROLE_CLIENTE`), fechaNacimiento, esDuoc.
2. **Repositorios (`cl.duoc.sonidovivo.repository`)**:
   - `ProductoRepository`: operaciones CRUD y filtros por categoría y nombre.
   - `UsuarioRepository`: búsqueda por email (`findByEmail`) y existencia (`existsByEmail`).
3. **Seguridad y JWT (`cl.duoc.sonidovivo.security`)**:
   - `SecurityConfig`: Configuración de Spring Security 6 con sesiones sin estado (`SessionCreationPolicy.STATELESS`), CORS habilitado para `http://localhost:5173`, protección de endpoints de modificación para `ROLE_ADMIN`.
   - `JwtUtils`: Generación de tokens HMAC-SHA256 con subject, rol y expiración de 24 horas.
   - `JwtAuthFilter`: Filtro por petición que extrae el token Bearer y autentica el contexto.
   - `UserDetailsServiceImpl`: Carga de usuario y mapeo de roles a authorities de Spring Security.
4. **Servicios y Controladores (`cl.duoc.sonidovivo.service` / `controller`)**:
   - `AuthService` y `AuthController`: `/api/auth/login`, `/api/auth/registro`, `/api/auth/me`. Valida mayoría de edad (>= 18 años) y detecta correos institucionales `@duocuc.cl` para asignar el beneficio `esDuoc = true`.
   - `ProductoService` y `ProductoController`: CRUD completo con respuestas HTTP RESTful (200, 201, 204, 400, 404).
5. **Pre-carga de Datos (`cl.duoc.sonidovivo.config.DataLoader`)**:
   - Sembrado automático de los 12 productos oficiales de audio profesional de Tienda Sonido Vivo.
   - Sembrado de usuarios iniciales de prueba (Admin y Cliente).

---

## 👥 Usuarios de Prueba Sembrados

| Nombre | Correo Electrónico | Contraseña | Rol | Beneficio Duoc |
| :--- | :--- | :--- | :---: | :---: |
| **Administrador Sonido Vivo** | `admin@sonidovivo.cl` | `duoc123` | `ROLE_ADMIN` | No |
| **Benjamín Gómez (Alumno)** | `alumno@duocuc.cl` | `duoc123` | `ROLE_CLIENTE` | Sí (`esDuoc: true`) |

---

## 🚀 Requisitos y Puesta en Marcha

### 1. Prerrequisitos
- **Java 21 LTS** configurado en el sistema (`JAVA_HOME`).
- **MySQL en XAMPP** iniciado en el puerto **`3307`** (o configurado en `src/main/resources/application.properties`).
- Base de datos `sonidovivo_db` (se crea automáticamente si no existe gracias al parámetro `createDatabaseIfNotExist=true`).

### 2. Ejecutar el Servidor
En una terminal ubicada en esta carpeta (`backend-sonidovivo`):

#### En Windows (PowerShell / CMD):
```powershell
.\mvnw.cmd spring-boot:run
```

#### En Linux / macOS:
```bash
./mvnw spring-boot:run
```

El servidor quedará escuchando peticiones en **`http://localhost:8080`**.

---

## 📖 Documentación Interactiva (Swagger UI)

Con el servidor en ejecución, ingresa a:  
👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### Cómo probar endpoints protegidos en Swagger:
1. Realiza una petición `POST /api/auth/login` con el usuario admin (`admin@sonidovivo.cl` / `duoc123`).
2. Copia el token JWT de la respuesta JSON (campo `token`).
3. Haz clic en el botón verde **Authorize 🔓** en la parte superior derecha de Swagger.
4. Pega el token en el campo `BearerAuth` y presiona **Authorize**.
5. Ahora podrás ejecutar `POST`, `PUT` y `DELETE` en `/api/productos` con permisos de administrador.

---

## 📡 Matriz Completa de Endpoints REST

### Autenticación y Perfil (`/api/auth`)
| Verbo | Endpoint | Permiso | Descripción | Retornos |
| :---: | :--- | :---: | :--- | :---: |
| **POST** | `/api/auth/login` | Público | Autenticación con email y password. Retorna JWT y datos de usuario. | `200 OK` / `401 Unauthorized` |
| **POST** | `/api/auth/registro` | Público | Registro de cliente. Valida >= 18 años y detecta `@duocuc.cl`. | `201 Created` / `400 Bad Request` |
| **GET** | `/api/auth/me` | Autenticado | Retorna el perfil del usuario autenticado vía token Bearer. | `200 OK` / `401 Unauthorized` |

### Catálogo de Productos (`/api/productos`)
| Verbo | Endpoint | Permiso | Descripción | Retornos |
| :---: | :--- | :---: | :--- | :---: |
| **GET** | `/api/productos` | Público | Listar productos del catálogo (soporta `?buscar=...`). | `200 OK` |
| **GET** | `/api/productos/{id}` | Público | Obtener producto por ID. | `200 OK` / `404 Not Found` |
| **GET** | `/api/productos/categoria/{cat}` | Público | Filtrar catálogo por categoría de audio. | `200 OK` |
| **POST** | `/api/productos` | `ROLE_ADMIN` | Crear un nuevo equipo en el catálogo. | `201 Created` / `400` / `403` |
| **PUT** | `/api/productos/{id}` | `ROLE_ADMIN` | Actualizar datos y stock de un equipo. | `200 OK` / `400` / `404` / `403` |
| **DELETE** | `/api/productos/{id}` | `ROLE_ADMIN` | Eliminar un equipo del catálogo. | `204 No Content` / `404` / `403` |

---

## 🧪 Pruebas Automatizadas

El proyecto cuenta con **14 pruebas automatizadas** de integración que validan el 100% de los flujos requeridos por la rúbrica EP3 (controladores, repositorios, seguridad, validación de edad y roles):

```powershell
.\mvnw.cmd test
```

**Resultado de ejecución:**
```
[INFO] Results:
[INFO] Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
