# DOCUMENTACIÓN COMPLETA - SISTEMA EDUTECH

## Índice
1. [Resumen General del Proyecto](#resumen-general-del-proyecto)
2. [Arquitectura del Sistema](#arquitectura-del-sistema)
3. [Tecnologías Utilizadas](#tecnologías-utilizadas)
4. [Microservicio CURSO](#microservicio-curso)
5. [Microservicio PAGO](#microservicio-pago)
6. [Microservicio USUARIO](#microservicio-usuario)
7. [Microservicio SISTEMA (API Gateway)](#microservicio-sistema-api-gateway)
8. [Microservicio TIPOUSUARIO](#microservicio-tipousuario)
9. [Configuraciones y Properties](#configuraciones-y-properties)
10. [Pruebas Unitarias](#pruebas-unitarias)
11. [Patrones de Diseño Implementados](#patrones-de-diseño-implementados)
12. [Flujos de Datos y Comunicación](#flujos-de-datos-y-comunicación)

---

## Resumen General del Proyecto

**EDUTECH** es un sistema educativo basado en microservicios desarrollado con Spring Boot que permite gestionar usuarios, cursos, pagos y tipos de usuario. El sistema implementa una arquitectura de microservicios con un API Gateway que centraliza y enriquece las consultas a los diferentes servicios.

### Propósito del Sistema
- Gestionar usuarios educativos (estudiantes, profesores, trabajadores)
- Administrar cursos y su información
- Controlar pagos y matrículas
- Proporcionar un punto único de acceso a través del microservicio sistema

---

## Arquitectura del Sistema

### Diagrama de Microservicios
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   FRONTEND      │    │   API GATEWAY   │    │   DATABASE      │
│                 │◄──►│   (SISTEMA)     │◄──►│   MySQL         │
│                 │    │   Puerto: 8085  │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                ┌───────────────┼───────────────┐
                │               │               │
        ┌───────▼──────┐ ┌──────▼─────┐ ┌──────▼─────┐
        │   USUARIO    │ │   CURSO    │ │    PAGO    │
        │ Puerto: 8081 │ │Puerto: 8084│ │Puerto: 8083│
        └──────────────┘ └────────────┘ └────────────┘
                │
        ┌───────▼──────┐
        │ TIPOUSUARIO  │
        │ Puerto: 8082 │
        └──────────────┘
```

### Puertos de los Microservicios
- **USUARIO**: 8081
- **TIPOUSUARIO**: 8082
- **PAGO**: 8083
- **CURSO**: 8084
- **SISTEMA (API Gateway)**: 8085

---

## Tecnologías Utilizadas

### Framework Principal
- **Spring Boot 3.x**: Framework principal para desarrollo de microservicios
- **Spring Data JPA**: Para persistencia y acceso a datos
- **Spring Web**: Para creación de APIs REST
- **Spring Test**: Para pruebas unitarias e integración

### Base de Datos
- **Laragon**

### Documentación API
- **Swagger/OpenAPI 3**: Documentación automática de APIs
- **SpringDoc**: Integración de Swagger con Spring Boot

### Pruebas
- **JUnit 5**: Framework de pruebas unitarias
- **Mockito**: Framework para mocking y simulación
- **MockMvc**: Para pruebas de controladores web

### Comunicación entre Microservicios
- **RestTemplate**: Cliente HTTP para comunicación síncrona entre servicios
- **HTTP/REST**: Protocolo de comunicación

### Herramientas de Desarrollo
- **Lombok**: Reducción de código boilerplate
- **Maven**: Gestión de dependencias y construcción
- **Jackson**: Serialización/deserialización JSON

---

## Microservicio CURSO

### Ubicación
`curso/src/main/java/com/edutech/curso/`

### Propósito
Gestiona toda la información relacionada con cursos educativos, incluyendo operaciones CRUD (Crear, Leer, Actualizar, Eliminar).

### Modelo de Datos

#### Clase Curso
```java
@Entity
@Table(name = "curso")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // Identificador único del curso
    
    @Column(name = "nombre_curso", nullable = false)
    private String nombreCurso;         // Nombre del curso
    
    @Column(name = "desc_curso", nullable = true)
    private String descripcionCurso;    // Descripción del curso
}
```

**Funcionalidad de cada campo:**
- `id`: Clave primaria autoincrementable para identificar únicamente cada curso
- `nombreCurso`: Almacena el nombre descriptivo del curso (obligatorio)
- `descripcionCurso`: Información detallada sobre el contenido del curso (opcional)

### Repositorio

#### CursoRepository
```java
@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
}
```

**Propósito**: Interfaz que extiende JpaRepository proporcionando automáticamente:
- `findAll()`: Obtiene todos los cursos
- `findById(Long id)`: Busca curso por ID
- `save(Curso curso)`: Guarda o actualiza un curso
- `deleteById(Long id)`: Elimina curso por ID
- `existsById(Long id)`: Verifica si existe un curso

### Servicio

#### CursoService
**Ubicación**: `curso/src/main/java/com/edutech/curso/service/CursoService.java`

##### Métodos Principales:

**1. obtenerTodos()**
```java
public List<Curso> obtenerTodos() {
    return cursoRepository.findAll();
}
```
- **Propósito**: Recupera todos los cursos de la base de datos
- **Retorna**: Lista completa de cursos disponibles
- **Uso**: Para mostrar catálogo de cursos

**2. obtenerPorId(Long id)**
```java
public Optional<Curso> obtenerPorId(Long id) {
    return cursoRepository.findById(id);
}
```
- **Propósito**: Busca un curso específico por su identificador
- **Parámetro**: `id` - Identificador único del curso
- **Retorna**: Optional<Curso> (puede estar vacío si no existe)
- **Uso**: Para mostrar detalles de un curso específico

**3. guardarCurso(Curso curso)**
```java
public Curso guardarCurso(Curso curso) {
    return cursoRepository.save(curso);
}
```
- **Propósito**: Crea un nuevo curso o actualiza uno existente
- **Parámetro**: `curso` - Objeto con la información del curso
- **Retorna**: Curso guardado con ID asignado
- **Uso**: Para crear nuevos cursos en el sistema

**4. eliminarCurso(Long id)**
```java
public void eliminarCurso(Long id) {
    cursoRepository.deleteById(id);
}
```
- **Propósito**: Elimina permanentemente un curso
- **Parámetro**: `id` - Identificador del curso a eliminar
- **Uso**: Para dar de baja cursos que ya no se ofrecen

**5. actualizarCurso(Long id, Curso curso)**
```java
public void actualizarCurso(Long id, Curso curso) {
    if (cursoRepository.existsById(id)) {
        curso.setId(id);
        cursoRepository.save(curso);
    } else {
        throw new RuntimeException("Curso no encontrado con ID: " + id);
    }
}
```
- **Propósito**: Actualiza información de un curso existente
- **Parámetros**: 
  - `id` - Identificador del curso a actualizar
  - `curso` - Nueva información del curso
- **Validación**: Verifica que el curso exista antes de actualizar
- **Manejo de errores**: Lanza excepción si el curso no existe

### Controlador

#### CursoController
**Ubicación**: `curso/src/main/java/com/edutech/curso/controller/CursoController.java`

##### Endpoints REST:

**1. POST /api/curso - Crear Curso**
```java
@PostMapping
public ResponseEntity<Curso> crearCurso(@RequestBody Curso curso) {
    Curso cursoCreado = cursoService.guardarCurso(curso);
    return ResponseEntity.ok(cursoCreado);
}
```
- **Propósito**: Endpoint para crear nuevos cursos
- **Método HTTP**: POST
- **Body**: JSON con datos del curso
- **Respuesta**: Curso creado con código 200
- **Uso**: Desde interfaces administrativas para agregar cursos

**2. GET /api/curso - Obtener Todos los Cursos**
```java
@GetMapping
public ResponseEntity<List<Curso>> obtenerTodosLosCursos() {
    List<Curso> cursos = cursoService.obtenerTodos();
    return ResponseEntity.ok(cursos);
}
```
- **Propósito**: Obtiene catálogo completo de cursos
- **Método HTTP**: GET
- **Respuesta**: Array JSON con todos los cursos
- **Uso**: Para mostrar lista de cursos disponibles

**3. GET /api/curso/{id} - Obtener Curso por ID**
```java
@GetMapping("/{id}")
public ResponseEntity<Curso> obtenerCursoPorId(@PathVariable Long id) {
    Optional<Curso> curso = cursoService.obtenerPorId(id);
    return curso.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
}
```
- **Propósito**: Obtiene detalles de un curso específico
- **Parámetro**: `id` en la URL
- **Respuesta**: 
  - 200 + curso si existe
  - 404 si no existe
- **Uso**: Para páginas de detalle de curso

**4. PUT /api/curso/{id} - Actualizar Curso**
```java
@PutMapping("/{id}")
public ResponseEntity<Void> actualizarCurso(@PathVariable Long id, @RequestBody Curso curso) {
    cursoService.actualizarCurso(id, curso);
    return ResponseEntity.ok().build();
}
```
- **Propósito**: Actualiza información de curso existente
- **Parámetros**: ID en URL + datos en body
- **Respuesta**: 200 si actualización exitosa
- **Uso**: Para modificar información de cursos

**5. DELETE /api/curso/{id} - Eliminar Curso**
```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
    cursoService.eliminarCurso(id);
    return ResponseEntity.ok().build();
}
```
- **Propósito**: Elimina un curso del sistema
- **Parámetro**: ID del curso en la URL
- **Respuesta**: 200 si eliminación exitosa
- **Uso**: Para dar de baja cursos

---

## Microservicio PAGO

### Ubicación
`pago/src/main/java/com/edutech/pago/`

### Propósito
Gestiona los pagos y matrículas de usuarios a cursos, manteniendo el estado de pago y relaciones entre usuarios y cursos.

### Modelo de Datos

#### Clase Pago
```java
@Entity
@Table(name = "pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // Identificador único del pago
    
    private Long usuarioRut;    // RUT del usuario que realiza el pago
    private Long cursoId;       // ID del curso al que se matricula
    private boolean estado;     // Estado del pago (true: pagado, false: pendiente)
}
```

**Funcionalidad de cada campo:**
- `id`: Identificador único de cada transacción de pago
- `usuarioRut`: Referencia al usuario (usa RUT como identificador)
- `cursoId`: Referencia al curso (clave foránea)
- `estado`: Indica si el pago está completado o pendiente

### Repositorio

#### PagoRepository
```java
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByUsuarioRut(Long usuarioRut);  // Pagos por usuario
    List<Pago> findByCursoId(Long cursoId);        // Pagos por curso
}
```

**Métodos personalizados:**
- `findByUsuarioRut()`: Encuentra todos los pagos de un usuario específico
- `findByCursoId()`: Encuentra todos los pagos para un curso específico

### Servicio

#### PagoService
**Ubicación**: `pago/src/main/java/com/edutech/pago/service/PagoService.java`

##### Métodos Principales:

**1. obtenerTodos()**
```java
public List<Pago> obtenerTodos() {
    return pagoRepository.findAll();
}
```
- **Propósito**: Obtiene todos los pagos del sistema
- **Uso**: Para reportes administrativos y auditoria

**2. obtenerPorId(Long id)**
```java
public Optional<Pago> obtenerPorId(Long id) {
    return pagoRepository.findById(id);
}
```
- **Propósito**: Busca un pago específico por ID
- **Uso**: Para consultar detalles de una transacción específica

**3. obtenerPorUsuarioRut(Long usuarioRut)**
```java
public List<Pago> obtenerPorUsuarioRut(Long usuarioRut) {
    return pagoRepository.findByUsuarioRut(usuarioRut);
}
```
- **Propósito**: Obtiene historial de pagos de un usuario
- **Uso**: Para mostrar cursos matriculados por usuario

**4. obtenerPorCursoId(Long cursoId)**
```java
public List<Pago> obtenerPorCursoId(Long cursoId) {
    return pagoRepository.findByCursoId(cursoId);
}
```
- **Propósito**: Obtiene todos los estudiantes matriculados en un curso
- **Uso**: Para listas de estudiantes por curso

**5. guardarPago(Pago pago)**
```java
public Pago guardarPago(Pago pago) {
    // Validar que el curso existe
    if (pago.getCursoId() == null || pago.getCursoId() <= 0) {
        throw new RuntimeException("El curso debe tener un ID válido");
    }
    
    // Validar que el usuario existe
    if (pago.getUsuarioRut() == null || pago.getUsuarioRut() <= 0) {
        throw new RuntimeException("El usuario debe tener un RUT válido");
    }

    return pagoRepository.save(pago);
}
```
- **Propósito**: Crea una nueva matrícula/pago
- **Validaciones**: 
  - Verifica que el curso ID sea válido
  - Verifica que el RUT del usuario sea válido
- **Uso**: Para matricular usuarios en cursos

**6. actualizarEstado(Long id, boolean estado)**
```java
public Pago actualizarEstado(Long id, boolean estado) {
    Optional<Pago> pagoOpt = pagoRepository.findById(id);
    if (pagoOpt.isPresent()) {
        Pago pago = pagoOpt.get();
        pago.setEstado(estado);
        return pagoRepository.save(pago);
    } else {
        throw new RuntimeException("Pago no encontrado con ID: " + id);
    }
}
```
- **Propósito**: Actualiza el estado de pago (pagado/pendiente)
- **Uso**: Para confirmar pagos o marcar como pendientes

**7. eliminarPago(Long id)**
```java
public void eliminarPago(Long id) {
    pagoRepository.deleteById(id);
}
```
- **Propósito**: Elimina un registro de pago
- **Uso**: Para cancelar matrículas

### Controlador

#### PagoController
**Ubicación**: `pago/src/main/java/com/edutech/pago/controller/PagoController.java`

##### Endpoints REST:

**1. GET /api/pagos - Obtener Todos los Pagos**
```java
@GetMapping
public List<Pago> obtenerTodos() {
    return pagoService.obtenerTodos();
}
```

**2. POST /api/pagos - Crear Pago**
```java
@PostMapping
public ResponseEntity<Pago> crearPago(@RequestBody Pago pago) {
    Pago pagoCreado = pagoService.guardarPago(pago);
    return ResponseEntity.ok(pagoCreado);
}
```

**3. GET /api/pagos/{id} - Obtener Pago por ID**
```java
@GetMapping("/{id}")
public ResponseEntity<Pago> obtenerPagoPorId(@PathVariable Long id) {
    Optional<Pago> pago = pagoService.obtenerPorId(id);
    return pago.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
}
```

**4. GET /api/pagos/usuario/{usuarioRut} - Pagos por Usuario**
```java
@GetMapping("/usuario/{usuarioRut}")
public List<Pago> obtenerPorUsuarioRut(@PathVariable Long usuarioRut) {
    return pagoService.obtenerPorUsuarioRut(usuarioRut);
}
```

**5. GET /api/pagos/curso/{cursoId} - Pagos por Curso**
```java
@GetMapping("/curso/{cursoId}")
public List<Pago> obtenerPorCursoId(@PathVariable Long cursoId) {
    return pagoService.obtenerPorCursoId(cursoId);
}
```

**6. PUT /api/pagos/{id}/estado - Actualizar Estado**
```java
@PutMapping("/{id}/estado")
public ResponseEntity<Pago> actualizarEstado(@PathVariable Long id, @RequestParam boolean estado) {
    Pago pagoActualizado = pagoService.actualizarEstado(id, estado);
    return ResponseEntity.ok(pagoActualizado);
}
```

**7. DELETE /api/pagos/{id} - Eliminar Pago**
```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
    pagoService.eliminarPago(id);
    return ResponseEntity.ok().build();
}
```

---

## Microservicio USUARIO

### Ubicación
`usuario/src/main/java/com/edutech/usuario/`

### Propósito
Gestiona toda la información de usuarios del sistema educativo, incluyendo estudiantes, profesores y personal administrativo.

### Modelo de Datos

#### Clase Usuario
```java
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    private Long rut;                           // RUT como clave primaria
    private String dv;                          // Dígito verificador del RUT
    private String nombre;                      // Nombre completo del usuario
    private String email;                       // Correo electrónico
    private String contrasena;                  // Contraseña (debe encriptarse)
    private LocalDateTime fechaRegistro;        // Fecha de creación del usuario
    private Long tipoUsuarioId;                 // Referencia al tipo de usuario
}
```

**Funcionalidad de cada campo:**
- `rut`: Identificador único del usuario (clave primaria)
- `dv`: Dígito verificador para validación del RUT
- `nombre`: Nombre completo para identificación
- `email`: Para comunicaciones y notificaciones
- `contrasena`: Credencial de acceso (debe implementarse encriptación)
- `fechaRegistro`: Auditoría de cuándo se creó el usuario
- `tipoUsuarioId`: Define el rol (estudiante, profesor, trabajador)

#### DTO TipoUsuarioDto
```java
public class TipoUsuarioDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private String permisos;
    private Boolean activo;
}
```
- **Propósito**: Transferir datos del microservicio TipoUsuario
- **Uso**: Para enriquecer información de usuario con su tipo

#### Clase UsuarioRequest
```java
public class UsuarioRequest {
    private Long rut;
    private String dv;
    private String nombre;
    private String email;
    private String contrasena;
    private Long tipoUsuarioId;
    private Long cursoId;           // Opcional: para matricula directa
    private Boolean estadoPago;     // Opcional: estado inicial del pago
}
```
- **Propósito**: DTO para recibir datos de creación/actualización
- **Campos adicionales**: Permite matricular directamente al crear usuario

### Repositorio

#### repository_usuario
```java
@Repository
public interface repository_usuario extends JpaRepository<Usuario, Long> {
}
```
**Nota**: Hereda todas las operaciones CRUD básicas de JpaRepository

### Servicios

#### UsuarioService
**Ubicación**: `usuario/src/main/java/com/edutech/usuario/service/UsuarioService.java`

##### Métodos Principales:

**1. crearUsuario(UsuarioRequest request)**
```java
public Usuario crearUsuario(UsuarioRequest request) {
    // Validar que el curso existe si se proporciona
    if (request.getCursoId() != null) {
        if (!microservicioService.verificarCursoExiste(request.getCursoId())) {
            throw new RuntimeException("El curso con ID " + request.getCursoId() + " no existe");
        }
    }
    
    // Crear usuario
    Usuario usuario = new Usuario();
    usuario.setRut(request.getRut());
    usuario.setDv(request.getDv());
    usuario.setNombre(request.getNombre());
    usuario.setEmail(request.getEmail());
    usuario.setContrasena(request.getContrasena());
    usuario.setFechaRegistro(LocalDateTime.now());
    usuario.setTipoUsuarioId(request.getTipoUsuarioId());
    
    // Guardar usuario PRIMERO
    Usuario usuarioGuardado = usuarioRepository.save(usuario);
    
    // Crear pago enriquecido si se asignó un curso
    if (request.getCursoId() != null) {
        // Lógica para crear pago automático
    }
    
    return usuarioGuardado;
}
```
- **Propósito**: Crea nuevo usuario con validaciones
- **Validaciones**:
  - Verifica que el curso existe (si se proporciona)
  - Valida datos obligatorios
- **Funcionalidades adicionales**:
  - Asigna fecha de registro automáticamente
  - Puede crear pago automático al matricular en curso
- **Integración**: Comunica con microservicio de curso y pago

**2. obtenerTodos()**
```java
public List<Usuario> obtenerTodos() {
    return usuarioRepository.findAll();
}
```
- **Propósito**: Obtiene lista completa de usuarios
- **Uso**: Para listados administrativos

**3. obtenerPorRut(String rut)**
```java
public Usuario obtenerPorRut(String rut) {
    Optional<Usuario> usuario = usuarioRepository.findById(Long.parseLong(rut));
    return usuario.orElse(null);
}
```
- **Propósito**: Busca usuario específico por RUT
- **Conversión**: Convierte String a Long para búsqueda
- **Retorno**: null si no existe (podría mejorarse con Optional)

**4. actualizarUsuario(String rut, UsuarioRequest request)**
```java
public Usuario actualizarUsuario(String rut, UsuarioRequest request) {
    Optional<Usuario> usuarioExistente = usuarioRepository.findById(Long.parseLong(rut));
    if (usuarioExistente.isPresent()) {
        Usuario usuario = usuarioExistente.get();
        usuario.setDv(request.getDv());
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setContrasena(request.getContrasena());
        usuario.setTipoUsuarioId(request.getTipoUsuarioId());
        
        return usuarioRepository.save(usuario);
    }
    return null;
}
```
- **Propósito**: Actualiza información de usuario existente
- **Validación**: Verifica que el usuario existe
- **Campos actualizables**: Todos excepto RUT y fecha de registro

**5. eliminarUsuario(String rut)**
```java
public boolean eliminarUsuario(String rut) {
    if (usuarioRepository.existsById(Long.parseLong(rut))) {
        usuarioRepository.deleteById(Long.parseLong(rut));
        return true;
    }
    return false;
}
```
- **Propósito**: Elimina usuario del sistema
- **Validación**: Verifica existencia antes de eliminar
- **Retorno**: boolean indicando éxito/fallo

**6. guardarUsuario(Usuario usuario)**
```java
public Usuario guardarUsuario(Usuario usuario) {
    return usuarioRepository.save(usuario);
}
```
- **Propósito**: Método genérico para guardar/actualizar
- **Uso**: Para operaciones internas del servicio

#### TipoUsuarioClient
**Ubicación**: `usuario/src/main/java/com/edutech/usuario/service/TipoUsuarioClient.java`

```java
@Service
public class TipoUsuarioClient {
    // Comunica con microservicio TipoUsuario
    // Para obtener información de tipos de usuario
}
```
- **Propósito**: Cliente para comunicación con microservicio TipoUsuario
- **Uso**: Para enriquecer datos de usuario con información de su tipo

### Controlador

#### UsuarioController
**Ubicación**: `usuario/src/main/java/com/edutech/usuario/controller/UsuarioController.java`

##### Endpoints REST principales:

**1. POST /api/usuarios - Crear Usuario**
- **Body**: UsuarioRequest JSON
- **Respuesta**: Usuario creado
- **Funcionalidad**: Crea usuario y opcionalmente lo matricula en curso

**2. GET /api/usuarios - Obtener Todos**
- **Respuesta**: Array de usuarios
- **Uso**: Listados administrativos

**3. GET /api/usuarios/{rut} - Obtener por RUT**
- **Parámetro**: RUT en URL
- **Respuesta**: Usuario específico o 404

**4. PUT /api/usuarios/{rut} - Actualizar Usuario**
- **Parámetros**: RUT en URL + UsuarioRequest en body
- **Respuesta**: Usuario actualizado

**5. DELETE /api/usuarios/{rut} - Eliminar Usuario**
- **Parámetro**: RUT en URL
- **Respuesta**: 200 si exitoso, 404 si no existe

---

## Microservicio SISTEMA (API Gateway)

### Ubicación
`sistema/src/main/java/com/edutech/sistema/`

### Propósito
Actúa como **API Gateway** y **servicio de agregación** que:
- Centraliza el acceso a todos los microservicios
- Enriquece datos combinando información de múltiples servicios
- Proporciona endpoints unificados para el frontend
- Implementa patrón de composición de servicios

### Modelos de Datos

#### Modelo Pago (Sistema)
```java
public class Pago {
    private Long id;
    private boolean estado;
    private Long usuarioRut;
    private Long cursoId;
}
```
- **Diferencia con Pago original**: Versión simplificada para transferencia

#### Modelo Usuario (Sistema)
```java
public class Usuario {
    private String rut;
    private String dv;
    private String nombre;
    private String email;
    private LocalDateTime fechaRegistro;
    private Long tipoUsuarioId;
}
```
- **Propósito**: Representación para agregación de datos

#### Modelo Curso (Sistema)
```java
public class Curso {
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("nombreCurso")
    private String nombreCurso;
    
    @JsonProperty("descripcionCurso")
    private String descripcionCurso;
}
```
- **Anotaciones JSON**: Para mapeo correcto desde servicios externos

#### Modelos Enriquecidos

##### PagoEnriquecido
```java
public class PagoEnriquecido {
    private Long id;
    private boolean estado;
    private Usuario usuario;        // Información completa del usuario
    private Curso curso;           // Información completa del curso
    private String nombreUsuario;  // Nombre del usuario para fácil acceso
    private String nombreCurso;    // Nombre del curso para fácil acceso
}
```
- **Propósito**: Combina datos de pago + usuario + curso en una sola respuesta

##### CursoEnriquecido
```java
public class CursoEnriquecido {
    private Long id;
    private String nombreCurso;
    private String descripcionCurso;
    private List<Usuario> estudiantes;    // Lista de usuarios matriculados
    private int totalEstudiantes;         // Contador de matriculados
}
```
- **Propósito**: Curso + lista de estudiantes matriculados

##### UsuarioEnriquecido
```java
public class UsuarioEnriquecido {
    private String rut;
    private String nombre;
    private String email;
    private LocalDateTime fechaRegistro;
    private TipoUsuario tipoUsuario;      // Información del tipo de usuario
    private List<Curso> cursosMatriculados; // Cursos en los que está matriculado
    private List<Pago> historialPagos;   // Historial completo de pagos
}
```
- **Propósito**: Usuario + tipo + cursos + historial de pagos

### Servicios

#### PagoService (Sistema)
**Ubicación**: `sistema/src/main/java/com/edutech/sistema/service/PagoService.java`

##### Métodos Principales:

**1. obtenerPagoPorId(Long id)**
```java
public Pago obtenerPagoPorId(Long id) {
    try {
        String url = "http://localhost:8083/api/pagos/" + id;
        Pago pago = restTemplate.getForObject(url, Pago.class);
        return pago;
    } catch (HttpClientErrorException.NotFound e) {
        logger.warn("Pago con ID {} no encontrado", id);
        return null;
    } catch (Exception e) {
        logger.error("Error al obtener pago: {}", e.getMessage());
        return null;
    }
}
```
- **Propósito**: Obtiene pago desde microservicio pago
- **Tecnología**: Usa RestTemplate para comunicación HTTP
- **Manejo de errores**: Captura excepciones de red y 404
- **Logging**: Registra errores para debugging

**2. obtenerTodosLosPagos()**
```java
public List<Pago> obtenerTodosLosPagos() {
    try {
        String url = "http://localhost:8083/api/pagos";
        Pago[] pagosArray = restTemplate.getForObject(url, Pago[].class);
        return pagosArray != null ? Arrays.asList(pagosArray) : new ArrayList<>();
    } catch (HttpClientErrorException.NotFound e) {
        logger.warn("Endpoint de pagos no encontrado");
        return new ArrayList<>();
    } catch (Exception e) {
        logger.error("Error al obtener todos los pagos: {}", e.getMessage());
        return new ArrayList<>();
    }
}
```
- **Propósito**: Obtiene lista completa de pagos
- **Manejo de nulls**: Retorna lista vacía si hay problemas
- **Robustez**: No falla si el microservicio está caído

#### CursoService (Sistema)
**Ubicación**: `sistema/src/main/java/com/edutech/sistema/service/CursoService.java`

##### Métodos Principales:

**1. obtenerCursoPorId(Long cursoId)**
```java
public Curso obtenerCursoPorId(Long cursoId) {
    try {
        String url = "http://localhost:8084/api/curso/" + cursoId;
        logger.info("Llamando a URL: {}", url);
        
        ResponseEntity<Curso> response = restTemplate.getForEntity(url, Curso.class);
        
        logger.info("Status Code: {}", response.getStatusCode());
        logger.info("Headers: {}", response.getHeaders());
        
        Curso curso = response.getBody();
        
        if (curso != null) {
            logger.info("DATOS DEL CURSO OBTENIDO:");
            logger.info("ID: {}", curso.getId());
            logger.info("Nombre: '{}'", curso.getNombreCurso());
            logger.info("Descripcion: '{}'", curso.getDescripcionCurso());
            
            if (curso.getNombreCurso() == null) {
                logger.error("PROBLEMA: El nombre del curso es NULL");
            }
            if (curso.getDescripcionCurso() == null) {
                logger.error("PROBLEMA: La descripcion del curso es NULL");
            }
        } else {
            logger.error("Curso retornado es null para ID: {}", cursoId);
        }
        
        return curso;
    } catch (ResourceAccessException e) {
        logger.error("ERROR: Microservicio de cursos no disponible en puerto 8084");
        logger.error("Verifica que el microservicio curso este ejecutandose");
        return null;
    } catch (HttpClientErrorException e) {
        logger.error("Error HTTP al obtener curso con ID {}: {} - {}", 
                    cursoId, e.getStatusCode(), e.getResponseBodyAsString());
        return null;
    } catch (Exception e) {
        logger.error("Error inesperado al obtener curso con ID {}: {}", cursoId, e.getMessage());
        e.printStackTrace();
        return null;
    }
}
```
- **Logging detallado**: Registra cada paso de la comunicación
- **Validación de datos**: Verifica que los campos no sean null
- **Múltiples tipos de excepción**: Maneja conexión, HTTP y errores generales
- **Debugging**: Información útil para diagnóstico de problemas

**2. obtenerTodosLosCursos()**
```java
public List<Curso> obtenerTodosLosCursos() {
    try {
        String url = "http://localhost:8084/api/curso";
        logger.info("Llamando a URL: {}", url);
        
        ResponseEntity<Curso[]> response = restTemplate.getForEntity(url, Curso[].class);
        Curso[] cursosArray = response.getBody();
        
        if (cursosArray != null) {
            logger.info("Se obtuvieron {} cursos", cursosArray.length);
            for (Curso curso : cursosArray) {
                logger.info("Curso: ID={}, Nombre='{}', Descripcion='{}'", 
                          curso.getId(), curso.getNombreCurso(), curso.getDescripcionCurso());
            }
            return Arrays.asList(cursosArray);
        } else {
            logger.warn("Array de cursos es null");
            return new ArrayList<>();
        }
        
    } catch (ResourceAccessException e) {
        logger.error("ERROR: Microservicio de cursos no disponible en puerto 8084");
        return new ArrayList<>();
    } catch (HttpClientErrorException e) {
        logger.error("Error HTTP al obtener todos los cursos: {} - {}", 
                    e.getStatusCode(), e.getResponseBodyAsString());
        return new ArrayList<>();
    } catch (Exception e) {
        logger.error("Error al obtener todos los cursos: {}", e.getMessage());
        e.printStackTrace();
        return new ArrayList<>();
    }
}
```
- **Iteración y logging**: Registra cada curso obtenido
- **Conteo**: Informa cantidad de cursos obtenidos
- **Resilencia**: Retorna lista vacía en caso de error

#### UsuarioService (Sistema)
**Ubicación**: `sistema/src/main/java/com/edutech/sistema/service/UsuarioService.java`

##### Métodos Principales:

**1. obtenerUsuarioPorRut(String rut)**
```java
public Usuario obtenerUsuarioPorRut(String rut) {
    try {
        String url = "http://localhost:8081/api/usuarios/" + rut;
        logger.info("Llamando a URL: {}", url);
        
        Usuario usuario = restTemplate.getForObject(url, Usuario.class);
        
        if (usuario != null) {
            logger.info("Usuario obtenido exitosamente - RUT: {}, Nombre: {}", 
                      usuario.getRut(), usuario.getNombre());
        } else {
            logger.warn("Usuario retornado es null para RUT: {}", rut);
        }
        
        return usuario;
    } catch (HttpClientErrorException.NotFound e) {
        logger.warn("Usuario con RUT {} no encontrado - 404", rut);
        return null;
    } catch (Exception e) {
        logger.error("Error al obtener usuario con RUT {}: {}", rut, e.getMessage());
        e.printStackTrace();
        return null;
    }
}
```
- **Búsqueda por RUT**: Utiliza RUT como identificador único
- **Manejo 404**: Distingue entre usuario no encontrado y errores de sistema

**2. obtenerTodosLosUsuarios()**
```java
public List<Usuario> obtenerTodosLosUsuarios() {
    try {
        String url = "http://localhost:8081/api/usuarios";
        logger.info("Llamando a URL: {}", url);
        
        Usuario[] usuariosArray = restTemplate.getForObject(url, Usuario[].class);
        
        if (usuariosArray != null) {
            logger.info("Se obtuvieron {} usuarios", usuariosArray.length);
            for (Usuario usuario : usuariosArray) {
                logger.info("Usuario: RUT={}, Nombre={}", usuario.getRut(), usuario.getNombre());
            }
        }
        
        return usuariosArray != null ? Arrays.asList(usuariosArray) : new ArrayList<>();
    } catch (Exception e) {
        logger.error("Error al obtener todos los usuarios: {}", e.getMessage());
        e.printStackTrace();
        return new ArrayList<>();
    }
}
```

#### EnriquecimientoService
**Ubicación**: `sistema/src/main/java/com/edutech/sistema/service/EnriquecimientoService.java`

```java
@Service
public class EnriquecimientoService {
    
    @Autowired
    private PagoService pagoService;
    
    @Autowired
    private CursoService cursoService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    public PagoEnriquecido enriquecerPago(Pago pago) {
        PagoEnriquecido enriquecido = new PagoEnriquecido();
        enriquecido.setId(pago.getId());
        enriquecido.setEstado(pago.isEstado());
        
        // Obtener información del usuario
        Usuario usuario = usuarioService.obtenerUsuarioPorRut(pago.getUsuarioRut().toString());
        enriquecido.setUsuario(usuario);
        enriquecido.setNombreUsuario(usuario != null ? usuario.getNombre() : "Usuario no encontrado");
        
        // Obtener información del curso
        Curso curso = cursoService.obtenerCursoPorId(pago.getCursoId());
        enriquecido.setCurso(curso);
        enriquecido.setNombreCurso(curso != null ? curso.getNombreCurso() : "Curso no encontrado");
        
        return enriquecido;
    }
    
    public List<PagoEnriquecido> enriquecerPagos(List<Pago> pagos) {
        return pagos.stream()
                   .map(this::enriquecerPago)
                   .collect(Collectors.toList());
    }
    
    // Métodos similares para CursoEnriquecido y UsuarioEnriquecido
}
```
- **Propósito**: Combina datos de múltiples microservicios
- **Patrón**: Composition pattern para agregación de datos
- **Stream API**: Usa programación funcional para transformaciones
- **Manejo de nulls**: Proporciona valores por defecto si no encuentra datos

#### SistemaService
**Ubicación**: `sistema/src/main/java/com/edutech/sistema/service/SistemaService.java`

##### Métodos de Utilidad:

**1. verificarCursoExiste(Long cursoId)**
```java
public boolean verificarCursoExiste(Long cursoId) {
    try {
        String url = "http://localhost:8084/api/curso/" + cursoId;
        restTemplate.getForObject(url, Object.class);
        return true;
    } catch (HttpClientErrorException.NotFound e) {
        logger.warn("Curso con ID {} no encontrado", cursoId);
        return false;
    } catch (Exception e) {
        logger.error("Error al verificar curso: {}", e.getMessage());
        return false;
    }
}
```
- **Propósito**: Validación de existencia de curso
- **Uso**: Antes de crear pagos o matrículas
- **Optimización**: No necesita datos completos, solo verificación

**2. crearPago(Long usuarioRut, Long cursoId, Boolean estadoPago)**
```java
public Long crearPago(Long usuarioRut, Long cursoId, Boolean estadoPago) {
    try {
        String url = "http://localhost:8083/api/pagos";
        
        Map<String, Object> pagoRequest = new HashMap<>();
        pagoRequest.put("usuarioRut", usuarioRut);
        pagoRequest.put("cursoId", cursoId);
        pagoRequest.put("estado", estadoPago != null ? estadoPago : false);
        
        Map<String, Object> response = restTemplate.postForObject(url, pagoRequest, Map.class);
        
        if (response != null && response.containsKey("id")) {
            return Long.valueOf(response.get("id").toString());
        }
        
        logger.warn("No se pudo obtener ID del pago creado");
        return null;
        
    } catch (Exception e) {
        logger.error("Error al crear pago: {}", e.getMessage());
        return null;
    }
}
```
- **Propósito**: Crea pagos desde sistema
- **Estructura de datos**: Usa Map para flexibilidad
- **Retorno**: ID del pago creado para referencia
- **Estado por defecto**: false si no se especifica

### Controlador

#### SistemaController
**Ubicación**: `sistema/src/main/java/com/edutech/sistema/controller/SistemaController.java`

##### Endpoints de Pagos:

**1. GET /api/sistema/pagos - Todos los Pagos Enriquecidos**
```java
@GetMapping("pagos")
public List<PagoEnriquecido> getAllPagos() {
    List<Pago> pagos = pagoService.obtenerTodosLosPagos();
    return enriquecimientoService.enriquecerPagos(pagos);
}
```
- **Propósito**: Pagos con información de usuario y curso
- **Valor agregado**: Una llamada obtiene datos de 3 microservicios

**2. GET /api/sistema/pagos/{id} - Pago Enriquecido por ID**
```java
@GetMapping("pagos/{id}")
public PagoEnriquecido getPago(@PathVariable Long id) {
    Pago pago = pagoService.obtenerPagoPorId(id);
    return pago != null ? enriquecimientoService.enriquecerPago(pago) : null;
}
```

##### Endpoints de Cursos:

**3. GET /api/sistema/cursos - Todos los Cursos Enriquecidos**
```java
@GetMapping("cursos")
public List<CursoEnriquecido> getAllCursos() {
    List<Curso> cursos = cursoService.obtenerTodosLosCursos();
    return enriquecimientoService.enriquecerCursos(cursos);
}
```

**4. GET /api/sistema/cursos/{id} - Curso Enriquecido por ID**
```java
@GetMapping("cursos/{id}")
public CursoEnriquecido getCurso(@PathVariable Long id) {
    return enriquecimientoService.enriquecerCursoPorId(id);
}
```

##### Endpoints de Usuarios:

**5. GET /api/sistema/usuarios - Todos los Usuarios Enriquecidos**
```java
@GetMapping("usuarios")
public List<UsuarioEnriquecido> getAllUsuarios() {
    List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
    return enriquecimientoService.enriquecerUsuarios(usuarios);
}
```

**6. GET /api/sistema/usuarios/{rut} - Usuario Enriquecido por RUT**
```java
@GetMapping("usuarios/{rut}")
public UsuarioEnriquecido getUsuario(@PathVariable String rut) {
    Usuario usuario = usuarioService.obtenerUsuarioPorRut(rut);
    return usuario != null ? enriquecimientoService.enriquecerUsuario(usuario) : null;
}
```

### Configuración

#### RestTemplate Configuration
```java
@Configuration
public class AppConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```
- **Propósito**: Configura cliente HTTP para comunicación entre servicios
- **Ámbito**: Singleton compartido por todos los servicios

---

## Microservicio TIPOUSUARIO

### Ubicación
`tipousuario/src/main/java/com/duoc/Fullstack3/`

### Propósito
Gestiona los diferentes tipos/roles de usuarios del sistema educativo (Estudiante, Profesor, Trabajador) con sus permisos específicos.

### Modelo de Datos

#### Clase TipoUsuario
```java
@Entity
@Table(name = "tipo_usuario")
public class TipoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // Identificador único
    
    private String nombre;              // Nombre del tipo (ESTUDIANTE, PROFESOR, etc.)
    private String descripcion;         // Descripción del rol
    private String permisos;            // JSON con permisos específicos
    private Boolean activo;             // Estado del tipo de usuario
}
```

**Funcionalidad de cada campo:**
- `id`: Clave primaria autoincrementable
- `nombre`: Identificador del rol (usado en lógica de negocio)
- `descripcion`: Texto descriptivo para interfaces de usuario
- `permisos`: JSON string con permisos específicos del rol
- `activo`: Permite habilitar/deshabilitar tipos de usuario

**Ejemplo de permisos JSON:**
```json
{
  "acceso_basico": true,
  "gestionar_clases": true,
  "acceso_administrativo": false
}
```

### Repositorio

#### TipoUsuarioRepository
```java
@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {
}
```

### Servicio

#### TipoUsuarioService
**Ubicación**: `tipousuario/src/main/java/com/duoc/Fullstack3/services/TipoUsuarioService.java`

##### Métodos Principales:

**1. inicializarTipos() - @PostConstruct**
```java
@PostConstruct
public void inicializarTipos() {
    if (tipoUsuarioRepository.count() == 0) {
        crearTiposBasicos();
    }
}
```
- **Anotación @PostConstruct**: Se ejecuta automáticamente al iniciar la aplicación
- **Propósito**: Asegura que existan tipos básicos en el sistema
- **Condición**: Solo crea si no existen tipos previamente
- **Uso**: Inicialización de datos maestros

**2. crearTiposBasicos()**
```java
private void crearTiposBasicos() {
    // ESTUDIANTE
    TipoUsuario estudiante = new TipoUsuario();
    estudiante.setNombre("ESTUDIANTE");
    estudiante.setDescripcion("Estudiante del sistema educativo");
    estudiante.setPermisos("{\"acceso_basico\":true}");
    estudiante.setActivo(true);
    tipoUsuarioRepository.save(estudiante);

    // PROFESOR
    TipoUsuario profesor = new TipoUsuario();
    profesor.setNombre("PROFESOR");
    profesor.setDescripcion("Profesor del sistema educativo");
    profesor.setPermisos("{\"acceso_avanzado\":true,\"gestionar_clases\":true}");
    profesor.setActivo(true);
    tipoUsuarioRepository.save(profesor);

    // TRABAJADOR
    TipoUsuario trabajador = new TipoUsuario();
    trabajador.setNombre("TRABAJADOR");
    trabajador.setDescripcion("Personal administrativo");
    trabajador.setPermisos("{\"acceso_administrativo\":true}");
    trabajador.setActivo(true);
    tipoUsuarioRepository.save(trabajador);

    System.out.println("Tipos básicos inicializados: ESTUDIANTE, PROFESOR, TRABAJADOR");
}
```
- **Tipos predefinidos**: Crea los 3 roles básicos del sistema
- **Permisos diferenciados**: Cada tipo tiene permisos específicos
- **Logging**: Confirma la inicialización exitosa

**3. obtenerTodos()**
```java
public List<TipoUsuario> obtenerTodos() {
    return tipoUsuarioRepository.findAll();
}
```
- **Propósito**: Lista todos los tipos de usuario disponibles
- **Uso**: Para selectores en formularios de usuario

**4. obtenerPorId(Long id)**
```java
public Optional<TipoUsuario> obtenerPorId(Long id) {
    return tipoUsuarioRepository.findById(id);
}
```
- **Propósito**: Obtiene tipo específico por ID
- **Retorno**: Optional para manejo seguro de null

**5. existeTipo(Long id)**
```java
public boolean existeTipo(Long id) {
    return tipoUsuarioRepository.existsById(id);
}
```
- **Propósito**: Validación de existencia de tipo
- **Uso**: Antes de asignar tipo a usuario
- **Optimización**: No carga el objeto completo, solo verifica existencia

### Estructura de Permisos

#### Permisos por Tipo:

**ESTUDIANTE:**
```json
{
  "acceso_basico": true
}
```
- Acceso básico al sistema
- Visualización de cursos
- Gestión de perfil personal

**PROFESOR:**
```json
{
  "acceso_avanzado": true,
  "gestionar_clases": true
}
```
- Acceso avanzado al sistema
- Gestión de clases y cursos
- Visualización de estudiantes

**TRABAJADOR:**
```json
{
  "acceso_administrativo": true
}
```
- Acceso administrativo
- Gestión de usuarios
- Reportes del sistema

---

## Configuraciones y Properties

### Configuración del Microservicio Sistema

#### application.properties
**Ubicación**: `sistema/src/main/resources/application.properties`

```properties
# Configuración de la aplicación
spring.application.name=sistema
server.port=8085

# Configuración de base de datos MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/edutech
spring.datasource.username=root
spring.datasource.password=

# Configuración de JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Configuración de Swagger/OpenAPI
springdoc.api-docs.enabled=true
spring.profiles.active=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/doc/swagger-ui.html
```

**Explicación de cada configuración:**

**Aplicación:**
- `spring.application.name=sistema`: Nombre del microservicio
- `server.port=8085`: Puerto de ejecución del API Gateway

**Base de Datos:**
- `spring.datasource.url`: URL de conexión a MySQL
- `spring.datasource.username/password`: Credenciales de acceso
- `hibernate.ddl-auto=update`: Actualiza schema automáticamente
- `show-sql=true`: Muestra queries SQL en logs
- `format_sql=true`: Formatea queries para legibilidad
- `dialect=MySQL8Dialect`: Optimizaciones para MySQL 8

**Documentación:**
- `springdoc.api-docs.enabled=true`: Habilita generación de docs OpenAPI
- `springdoc.swagger-ui.enabled=true`: Habilita interfaz Swagger UI
- `springdoc.swagger-ui.path`: Ruta personalizada para Swagger

### Configuración Maven

#### Dependencias Principales (pom.xml)
```xml
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- Base de datos -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    
    <!-- Documentación -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    </dependency>
    
    <!-- Utilidades -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    
    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## Pruebas Unitarias

### Estrategia de Testing

El proyecto implementa una estrategia completa de testing que incluye:

1. **Pruebas de Servicio (Lógica de Negocio)**
2. **Pruebas de Controlador (Endpoints REST)**
3. **Pruebas de Integración (Comunicación entre servicios)**

### Tecnologías de Testing

#### JUnit 5
- **@ExtendWith(MockitoExtension.class)**: Integración con Mockito
- **@Test**: Marca métodos de prueba
- **@BeforeEach**: Configuración antes de cada prueba
- **Assertions**: assertEquals, assertNotNull, assertTrue, etc.

#### Mockito
- **@Mock**: Crea mocks de dependencias
- **@InjectMocks**: Inyecta mocks en clase bajo prueba
- **@MockitoBean**: Mock para contexto Spring
- **when().thenReturn()**: Configuración de comportamiento
- **verify()**: Verificación de llamadas a métodos

#### Spring Test
- **@WebMvcTest**: Pruebas de controladores web
- **@ContextConfiguration**: Configuración de contexto de prueba
- **MockMvc**: Simulación de peticiones HTTP
- **jsonPath()**: Validación de respuestas JSON

### Ejemplos de Pruebas

#### Prueba de Servicio - PagoService
```java
@ExtendWith(MockitoExtension.class) 
public class pagoServicesTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;
    
    private Pago pago1;
    private Pago pago2;

    @BeforeEach
    void setUp() {
        pago1 = new Pago();
        pago1.setId(1L);
        pago1.setUsuarioRut(12345678L);
        pago1.setCursoId(101L);
        pago1.setEstado(true);
        
        pago2 = new Pago();
        pago2.setId(2L);
        pago2.setUsuarioRut(87654321L);
        pago2.setCursoId(102L);
        pago2.setEstado(false);
    }

    @Test 
    void obtenerTodos_retornaListaPagos(){
        // Arrange
        ArrayList<Pago> pagosTest = new ArrayList<>(Arrays.asList(pago1, pago2));
        when(pagoRepository.findAll()).thenReturn(pagosTest);
        
        // Act
        List<Pago> resultadoObtenido = pagoService.obtenerTodos();
        
        // Assert
        assertNotNull(resultadoObtenido);
        assertEquals(2, resultadoObtenido.size(), "La Lista deberia contener 2 pagos");
        assertEquals(1L, resultadoObtenido.get(0).getId());
        assertEquals(12345678L, resultadoObtenido.get(0).getUsuarioRut());
        assertTrue(resultadoObtenido.get(0).isEstado());
        
        verify(pagoRepository, times(1)).findAll();
    }
}
```

**Explicación de la prueba:**
- **Arrange**: Configura datos de prueba y comportamiento de mocks
- **Act**: Ejecuta el método bajo prueba
- **Assert**: Verifica resultados y comportamientos esperados
- **verify()**: Confirma que se llamaron métodos específicos

#### Prueba de Controlador - PagoController
```java
@WebMvcTest(PagoController.class)
public class pagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PagoService pagoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void guardarPago_retornaPagoCreado() throws Exception {
        // Arrange
        when(pagoService.guardarPago(any(Pago.class))).thenReturn(pago1);
        
        // Act & Assert
        mockMvc.perform(post("/api/pagos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pago1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.usuarioRut").value(12345678L))
                .andExpect(jsonPath("$.estado").value(true));
        
        verify(pagoService, times(1)).guardarPago(any(Pago.class));
    }
}
```

**Explicación de la prueba:**
- **@WebMvcTest**: Carga solo contexto web para el controlador
- **MockMvc**: Simula peticiones HTTP sin levantar servidor
- **jsonPath()**: Valida campos específicos de la respuesta JSON
- **ObjectMapper**: Convierte// filepath: DOCUMENTACION_COMPLETA_EDUTECH.md
# DOCUMENTACIÓN COMPLETA - SISTEMA EDUTECH

## Índice
1. [Resumen General del Proyecto](#resumen-general-del-proyecto)
2. [Arquitectura del Sistema](#arquitectura-del-sistema)
3. [Tecnologías Utilizadas](#tecnologías-utilizadas)
4. [Microservicio CURSO](#microservicio-curso)
5. [Microservicio PAGO](#microservicio-pago)
6. [Microservicio USUARIO](#microservicio-usuario)
7. [Microservicio SISTEMA (API Gateway)](#microservicio-sistema-api-gateway)
8. [Microservicio TIPOUSUARIO](#microservicio-tipousuario)
9. [Configuraciones y Properties](#configuraciones-y-properties)
10. [Pruebas Unitarias](#pruebas-unitarias)
11. [Patrones de Diseño Implementados](#patrones-de-diseño-implementados)
12. [Flujos de Datos y Comunicación](#flujos-de-datos-y-comunicación)

---

## Resumen General del Proyecto

**EDUTECH** es un sistema educativo basado en microservicios desarrollado con Spring Boot que permite gestionar usuarios, cursos, pagos y tipos de usuario. El sistema implementa una arquitectura de microservicios con un API Gateway que centraliza y enriquece las consultas a los diferentes servicios.

### Propósito del Sistema
- Gestionar usuarios educativos (estudiantes, profesores, trabajadores)
- Administrar cursos y su información
- Controlar pagos y matrículas
- Proporcionar un punto único de acceso a través del microservicio sistema

---

## Arquitectura del Sistema

### Diagrama de Microservicios
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   FRONTEND      │    │   API GATEWAY   │    │   DATABASE      │
│                 │◄──►│   (SISTEMA)     │◄──►│   MySQL         │
│                 │    │   Puerto: 8085  │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                ┌───────────────┼───────────────┐
                │               │               │
        ┌───────▼──────┐ ┌──────▼─────┐ ┌──────▼─────┐
        │   USUARIO    │ │   CURSO    │ │    PAGO    │
        │ Puerto: 8081 │ │Puerto: 8084│ │Puerto: 8083│
        └──────────────┘ └────────────┘ └────────────┘
                │
        ┌───────▼──────┐
        │ TIPOUSUARIO  │
        │ Puerto: 8082 │
        └──────────────┘
```

### Puertos de los Microservicios
- **USUARIO**: 8081
- **TIPOUSUARIO**: 8082
- **PAGO**: 8083
- **CURSO**: 8084
- **SISTEMA (API Gateway)**: 8085

---

## Tecnologías Utilizadas

### Framework Principal
- **Spring Boot 3.x**: Framework principal para desarrollo de microservicios
- **Spring Data JPA**: Para persistencia y acceso a datos
- **Spring Web**: Para creación de APIs REST
- **Spring Test**: Para pruebas unitarias e integración

### Base de Datos
- **MySQL 8**: Sistema de gestión de base de datos relacional
- **Hibernate**: ORM (Object-Relational Mapping)

### Documentación API
- **Swagger/OpenAPI 3**: Documentación automática de APIs
- **SpringDoc**: Integración de Swagger con Spring Boot

### Pruebas
- **JUnit 5**: Framework de pruebas unitarias
- **Mockito**: Framework para mocking y simulación
- **MockMvc**: Para pruebas de controladores web

### Comunicación entre Microservicios
- **RestTemplate**: Cliente HTTP para comunicación síncrona entre servicios
- **HTTP/REST**: Protocolo de comunicación

### Herramientas de Desarrollo
- **Lombok**: Reducción de código boilerplate
- **Maven**: Gestión de dependencias y construcción
- **Jackson**: Serialización/deserialización JSON

---

## Microservicio CURSO

### Ubicación
`curso/src/main/java/com/edutech/curso/`

### Propósito
Gestiona toda la información relacionada con cursos educativos, incluyendo operaciones CRUD (Crear, Leer, Actualizar, Eliminar).

### Modelo de Datos

#### Clase Curso
```java
@Entity
@Table(name = "curso")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // Identificador único del curso
    
    @Column(name = "nombre_curso", nullable = false)
    private String nombreCurso;         // Nombre del curso
    
    @Column(name = "desc_curso", nullable = true)
    private String descripcionCurso;    // Descripción del curso
}
```

**Funcionalidad de cada campo:**
- `id`: Clave primaria autoincrementable para identificar únicamente cada curso
- `nombreCurso`: Almacena el nombre descriptivo del curso (obligatorio)
- `descripcionCurso`: Información detallada sobre el contenido del curso (opcional)

### Repositorio

#### CursoRepository
```java
@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
}
```

**Propósito**: Interfaz que extiende JpaRepository proporcionando automáticamente:
- `findAll()`: Obtiene todos los cursos
- `findById(Long id)`: Busca curso por ID
- `save(Curso curso)`: Guarda o actualiza un curso
- `deleteById(Long id)`: Elimina curso por ID
- `existsById(Long id)`: Verifica si existe un curso

### Servicio

#### CursoService
**Ubicación**: `curso/src/main/java/com/edutech/curso/service/CursoService.java`

##### Métodos Principales:

**1. obtenerTodos()**
```java
public List<Curso> obtenerTodos() {
    return cursoRepository.findAll();
}
```
- **Propósito**: Recupera todos los cursos de la base de datos
- **Retorna**: Lista completa de cursos disponibles
- **Uso**: Para mostrar catálogo de cursos

**2. obtenerPorId(Long id)**
```java
public Optional<Curso> obtenerPorId(Long id) {
    return cursoRepository.findById(id);
}
```
- **Propósito**: Busca un curso específico por su identificador
- **Parámetro**: `id` - Identificador único del curso
- **Retorna**: Optional<Curso> (puede estar vacío si no existe)
- **Uso**: Para mostrar detalles de un curso específico

**3. guardarCurso(Curso curso)**
```java
public Curso guardarCurso(Curso curso) {
    return cursoRepository.save(curso);
}
```
- **Propósito**: Crea un nuevo curso o actualiza uno existente
- **Parámetro**: `curso` - Objeto con la información del curso
- **Retorna**: Curso guardado con ID asignado
- **Uso**: Para crear nuevos cursos en el sistema

**4. eliminarCurso(Long id)**
```java
public void eliminarCurso(Long id) {
    cursoRepository.deleteById(id);
}
```
- **Propósito**: Elimina permanentemente un curso
- **Parámetro**: `id` - Identificador del curso a eliminar
- **Uso**: Para dar de baja cursos que ya no se ofrecen

**5. actualizarCurso(Long id, Curso curso)**
```java
public void actualizarCurso(Long id, Curso curso) {
    if (cursoRepository.existsById(id)) {
        curso.setId(id);
        cursoRepository.save(curso);
    } else {
        throw new RuntimeException("Curso no encontrado con ID: " + id);
    }
}
```
- **Propósito**: Actualiza información de un curso existente
- **Parámetros**: 
  - `id` - Identificador del curso a actualizar
  - `curso` - Nueva información del curso
- **Validación**: Verifica que el curso exista antes de actualizar
- **Manejo de errores**: Lanza excepción si el curso no existe

### Controlador

#### CursoController
**Ubicación**: `curso/src/main/java/com/edutech/curso/controller/CursoController.java`

##### Endpoints REST:

**1. POST /api/curso - Crear Curso**
```java
@PostMapping
public ResponseEntity<Curso> crearCurso(@RequestBody Curso curso) {
    Curso cursoCreado = cursoService.guardarCurso(curso);
    return ResponseEntity.ok(cursoCreado);
}
```
- **Propósito**: Endpoint para crear nuevos cursos
- **Método HTTP**: POST
- **Body**: JSON con datos del curso
- **Respuesta**: Curso creado con código 200
- **Uso**: Desde interfaces administrativas para agregar cursos

**2. GET /api/curso - Obtener Todos los Cursos**
```java
@GetMapping
public ResponseEntity<List<Curso>> obtenerTodosLosCursos() {
    List<Curso> cursos = cursoService.obtenerTodos();
    return ResponseEntity.ok(cursos);
}
```
- **Propósito**: Obtiene catálogo completo de cursos
- **Método HTTP**: GET
- **Respuesta**: Array JSON con todos los cursos
- **Uso**: Para mostrar lista de cursos disponibles

**3. GET /api/curso/{id} - Obtener Curso por ID**
```java
@GetMapping("/{id}")
public ResponseEntity<Curso> obtenerCursoPorId(@PathVariable Long id) {
    Optional<Curso> curso = cursoService.obtenerPorId(id);
    return curso.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
}
```
- **Propósito**: Obtiene detalles de un curso específico
- **Parámetro**: `id` en la URL
- **Respuesta**: 
  - 200 + curso si existe
  - 404 si no existe
- **Uso**: Para páginas de detalle de curso

**4. PUT /api/curso/{id} - Actualizar Curso**
```java
@PutMapping("/{id}")
public ResponseEntity<Void> actualizarCurso(@PathVariable Long id, @RequestBody Curso curso) {
    cursoService.actualizarCurso(id, curso);
    return ResponseEntity.ok().build();
}
```
- **Propósito**: Actualiza información de curso existente
- **Parámetros**: ID en URL + datos en body
- **Respuesta**: 200 si actualización exitosa
- **Uso**: Para modificar información de cursos

**5. DELETE /api/curso/{id} - Eliminar Curso**
```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
    cursoService.eliminarCurso(id);
    return ResponseEntity.ok().build();
}
```
- **Propósito**: Elimina un curso del sistema
- **Parámetro**: ID del curso en la URL
- **Respuesta**: 200 si eliminación exitosa
- **Uso**: Para dar de baja cursos

---

## Microservicio PAGO

### Ubicación
`pago/src/main/java/com/edutech/pago/`

### Propósito
Gestiona los pagos y matrículas de usuarios a cursos, manteniendo el estado de pago y relaciones entre usuarios y cursos.

### Modelo de Datos

#### Clase Pago
```java
@Entity
@Table(name = "pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // Identificador único del pago
    
    private Long usuarioRut;    // RUT del usuario que realiza el pago
    private Long cursoId;       // ID del curso al que se matricula
    private boolean estado;     // Estado del pago (true: pagado, false: pendiente)
}
```

**Funcionalidad de cada campo:**
- `id`: Identificador único de cada transacción de pago
- `usuarioRut`: Referencia al usuario (usa RUT como identificador)
- `cursoId`: Referencia al curso (clave foránea)
- `estado`: Indica si el pago está completado o pendiente

### Repositorio

#### PagoRepository
```java
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByUsuarioRut(Long usuarioRut);  // Pagos por usuario
    List<Pago> findByCursoId(Long cursoId);        // Pagos por curso
}
```

**Métodos personalizados:**
- `findByUsuarioRut()`: Encuentra todos los pagos de un usuario específico
- `findByCursoId()`: Encuentra todos los pagos para un curso específico

### Servicio

#### PagoService
**Ubicación**: `pago/src/main/java/com/edutech/pago/service/PagoService.java`

##### Métodos Principales:

**1. obtenerTodos()**
```java
public List<Pago> obtenerTodos() {
    return pagoRepository.findAll();
}
```
- **Propósito**: Obtiene todos los pagos del sistema
- **Uso**: Para reportes administrativos y auditoria

**2. obtenerPorId(Long id)**
```java
public Optional<Pago> obtenerPorId(Long id) {
    return pagoRepository.findById(id);
}
```
- **Propósito**: Busca un pago específico por ID
- **Uso**: Para consultar detalles de una transacción específica

**3. obtenerPorUsuarioRut(Long usuarioRut)**
```java
public List<Pago> obtenerPorUsuarioRut(Long usuarioRut) {
    return pagoRepository.findByUsuarioRut(usuarioRut);
}
```
- **Propósito**: Obtiene historial de pagos de un usuario
- **Uso**: Para mostrar cursos matriculados por usuario

**4. obtenerPorCursoId(Long cursoId)**
```java
public List<Pago> obtenerPorCursoId(Long cursoId) {
    return pagoRepository.findByCursoId(cursoId);
}
```
- **Propósito**: Obtiene todos los estudiantes matriculados en un curso
- **Uso**: Para listas de estudiantes por curso

**5. guardarPago(Pago pago)**
```java
public Pago guardarPago(Pago pago) {
    // Validar que el curso existe
    if (pago.getCursoId() == null || pago.getCursoId() <= 0) {
        throw new RuntimeException("El curso debe tener un ID válido");
    }
    
    // Validar que el usuario existe
    if (pago.getUsuarioRut() == null || pago.getUsuarioRut() <= 0) {
        throw new RuntimeException("El usuario debe tener un RUT válido");
    }

    return pagoRepository.save(pago);
}
```
- **Propósito**: Crea una nueva matrícula/pago
- **Validaciones**: 
  - Verifica que el curso ID sea válido
  - Verifica que el RUT del usuario sea válido
- **Uso**: Para matricular usuarios en cursos

**6. actualizarEstado(Long id, boolean estado)**
```java
public Pago actualizarEstado(Long id, boolean estado) {
    Optional<Pago> pagoOpt = pagoRepository.findById(id);
    if (pagoOpt.isPresent()) {
        Pago pago = pagoOpt.get();
        pago.setEstado(estado);
        return pagoRepository.save(pago);
    } else {
        throw new RuntimeException("Pago no encontrado con ID: " + id);
    }
}
```
- **Propósito**: Actualiza el estado de pago (pagado/pendiente)
- **Uso**: Para confirmar pagos o marcar como pendientes

**7. eliminarPago(Long id)**
```java
public void eliminarPago(Long id) {
    pagoRepository.deleteById(id);
}
```
- **Propósito**: Elimina un registro de pago
- **Uso**: Para cancelar matrículas

### Controlador

#### PagoController
**Ubicación**: `pago/src/main/java/com/edutech/pago/controller/PagoController.java`

##### Endpoints REST:

**1. GET /api/pagos - Obtener Todos los Pagos**
```java
@GetMapping
public List<Pago> obtenerTodos() {
    return pagoService.obtenerTodos();
}
```

**2. POST /api/pagos - Crear Pago**
```java
@PostMapping
public ResponseEntity<Pago> crearPago(@RequestBody Pago pago) {
    Pago pagoCreado = pagoService.guardarPago(pago);
    return ResponseEntity.ok(pagoCreado);
}
```

**3. GET /api/pagos/{id} - Obtener Pago por ID**
```java
@GetMapping("/{id}")
public ResponseEntity<Pago> obtenerPagoPorId(@PathVariable Long id) {
    Optional<Pago> pago = pagoService.obtenerPorId(id);
    return pago.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
}
```

**4. GET /api/pagos/usuario/{usuarioRut} - Pagos por Usuario**
```java
@GetMapping("/usuario/{usuarioRut}")
public List<Pago> obtenerPorUsuarioRut(@PathVariable Long usuarioRut) {
    return pagoService.obtenerPorUsuarioRut(usuarioRut);
}
```

**5. GET /api/pagos/curso/{cursoId} - Pagos por Curso**
```java
@GetMapping("/curso/{cursoId}")
public List<Pago> obtenerPorCursoId(@PathVariable Long cursoId) {
    return pagoService.obtenerPorCursoId(cursoId);
}
```

**6. PUT /api/pagos/{id}/estado - Actualizar Estado**
```java
@PutMapping("/{id}/estado")
public ResponseEntity<Pago> actualizarEstado(@PathVariable Long id, @RequestParam boolean estado) {
    Pago pagoActualizado = pagoService.actualizarEstado(id, estado);
    return ResponseEntity.ok(pagoActualizado);
}
```

**7. DELETE /api/pagos/{id} - Eliminar Pago**
```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
    pagoService.eliminarPago(id);
    return ResponseEntity.ok().build();
}
```

---

## Microservicio USUARIO

### Ubicación
`usuario/src/main/java/com/edutech/usuario/`

### Propósito
Gestiona toda la información de usuarios del sistema educativo, incluyendo estudiantes, profesores y personal administrativo.

### Modelo de Datos

#### Clase Usuario
```java
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    private Long rut;                           // RUT como clave primaria
    private String dv;                          // Dígito verificador del RUT
    private String nombre;                      // Nombre completo del usuario
    private String email;                       // Correo electrónico
    private String contrasena;                  // Contraseña (debe encriptarse)
    private LocalDateTime fechaRegistro;        // Fecha de creación del usuario
    private Long tipoUsuarioId;                 // Referencia al tipo de usuario
}
```

**Funcionalidad de cada campo:**
- `rut`: Identificador único del usuario (clave primaria)
- `dv`: Dígito verificador para validación del RUT
- `nombre`: Nombre completo para identificación
- `email`: Para comunicaciones y notificaciones
- `contrasena`: Credencial de acceso (debe implementarse encriptación)
- `fechaRegistro`: Auditoría de cuándo se creó el usuario
- `tipoUsuarioId`: Define el rol (estudiante, profesor, trabajador)

#### DTO TipoUsuarioDto
```java
public class TipoUsuarioDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private String permisos;
    private Boolean activo;
}
```
- **Propósito**: Transferir datos del microservicio TipoUsuario
- **Uso**: Para enriquecer información de usuario con su tipo

#### Clase UsuarioRequest
```java
public class UsuarioRequest {
    private Long rut;
    private String dv;
    private String nombre;
    private String email;
    private String contrasena;
    private Long tipoUsuarioId;
    private Long cursoId;           // Opcional: para matricula directa
    private Boolean estadoPago;     // Opcional: estado inicial del pago
}
```
- **Propósito**: DTO para recibir datos de creación/actualización
- **Campos adicionales**: Permite matricular directamente al crear usuario

### Repositorio

#### repository_usuario
```java
@Repository
public interface repository_usuario extends JpaRepository<Usuario, Long> {
}
```
**Nota**: Hereda todas las operaciones CRUD básicas de JpaRepository

### Servicios

#### UsuarioService
**Ubicación**: `usuario/src/main/java/com/edutech/usuario/service/UsuarioService.java`

##### Métodos Principales:

**1. crearUsuario(UsuarioRequest request)**
```java
public Usuario crearUsuario(UsuarioRequest request) {
    // Validar que el curso existe si se proporciona
    if (request.getCursoId() != null) {
        if (!microservicioService.verificarCursoExiste(request.getCursoId())) {
            throw new RuntimeException("El curso con ID " + request.getCursoId() + " no existe");
        }
    }
    
    // Crear usuario
    Usuario usuario = new Usuario();
    usuario.setRut(request.getRut());
    usuario.setDv(request.getDv());
    usuario.setNombre(request.getNombre());
    usuario.setEmail(request.getEmail());
    usuario.setContrasena(request.getContrasena());
    usuario.setFechaRegistro(LocalDateTime.now());
    usuario.setTipoUsuarioId(request.getTipoUsuarioId());
    
    // Guardar usuario PRIMERO
    Usuario usuarioGuardado = usuarioRepository.save(usuario);
    
    // Crear pago enriquecido si se asignó un curso
    if (request.getCursoId() != null) {
        // Lógica para crear pago automático
    }
    
    return usuarioGuardado;
}
```
- **Propósito**: Crea nuevo usuario con validaciones
- **Validaciones**:
  - Verifica que el curso existe (si se proporciona)
  - Valida datos obligatorios
- **Funcionalidades adicionales**:
  - Asigna fecha de registro automáticamente
  - Puede crear pago automático al matricular en curso
- **Integración**: Comunica con microservicio de curso y pago

**2. obtenerTodos()**
```java
public List<Usuario> obtenerTodos() {
    return usuarioRepository.findAll();
}
```
- **Propósito**: Obtiene lista completa de usuarios
- **Uso**: Para listados administrativos

**3. obtenerPorRut(String rut)**
```java
public Usuario obtenerPorRut(String rut) {
    Optional<Usuario> usuario = usuarioRepository.findById(Long.parseLong(rut));
    return usuario.orElse(null);
}
```
- **Propósito**: Busca usuario específico por RUT
- **Conversión**: Convierte String a Long para búsqueda
- **Retorno**: null si no existe (podría mejorarse con Optional)

**4. actualizarUsuario(String rut, UsuarioRequest request)**
```java
public Usuario actualizarUsuario(String rut, UsuarioRequest request) {
    Optional<Usuario> usuarioExistente = usuarioRepository.findById(Long.parseLong(rut));
    if (usuarioExistente.isPresent()) {
        Usuario usuario = usuarioExistente.get();
        usuario.setDv(request.getDv());
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setContrasena(request.getContrasena());
        usuario.setTipoUsuarioId(request.getTipoUsuarioId());
        
        return usuarioRepository.save(usuario);
    }
    return null;
}
```
- **Propósito**: Actualiza información de usuario existente
- **Validación**: Verifica que el usuario existe
- **Campos actualizables**: Todos excepto RUT y fecha de registro

**5. eliminarUsuario(String rut)**
```java
public boolean eliminarUsuario(String rut) {
    if (usuarioRepository.existsById(Long.parseLong(rut))) {
        usuarioRepository.deleteById(Long.parseLong(rut));
        return true;
    }
    return false;
}
```
- **Propósito**: Elimina usuario del sistema
- **Validación**: Verifica existencia antes de eliminar
- **Retorno**: boolean indicando éxito/fallo

**6. guardarUsuario(Usuario usuario)**
```java
public Usuario guardarUsuario(Usuario usuario) {
    return usuarioRepository.save(usuario);
}
```
- **Propósito**: Método genérico para guardar/actualizar
- **Uso**: Para operaciones internas del servicio

#### TipoUsuarioClient
**Ubicación**: `usuario/src/main/java/com/edutech/usuario/service/TipoUsuarioClient.java`

```java
@Service
public class TipoUsuarioClient {
    // Comunica con microservicio TipoUsuario
    // Para obtener información de tipos de usuario
}
```
- **Propósito**: Cliente para comunicación con microservicio TipoUsuario
- **Uso**: Para enriquecer datos de usuario con información de su tipo

### Controlador

#### UsuarioController
**Ubicación**: `usuario/src/main/java/com/edutech/usuario/controller/UsuarioController.java`

##### Endpoints REST principales:

**1. POST /api/usuarios - Crear Usuario**
- **Body**: UsuarioRequest JSON
- **Respuesta**: Usuario creado
- **Funcionalidad**: Crea usuario y opcionalmente lo matricula en curso

**2. GET /api/usuarios - Obtener Todos**
- **Respuesta**: Array de usuarios
- **Uso**: Listados administrativos

**3. GET /api/usuarios/{rut} - Obtener por RUT**
- **Parámetro**: RUT en URL
- **Respuesta**: Usuario específico o 404

**4. PUT /api/usuarios/{rut} - Actualizar Usuario**
- **Parámetros**: RUT en URL + UsuarioRequest en body
- **Respuesta**: Usuario actualizado

**5. DELETE /api/usuarios/{rut} - Eliminar Usuario**
- **Parámetro**: RUT en URL
- **Respuesta**: 200 si exitoso, 404 si no existe

---

## Microservicio SISTEMA (API Gateway)

### Ubicación
`sistema/src/main/java/com/edutech/sistema/`

### Propósito
Actúa como **API Gateway** y **servicio de agregación** que:
- Centraliza el acceso a todos los microservicios
- Enriquece datos combinando información de múltiples servicios
- Proporciona endpoints unificados para el frontend
- Implementa patrón de composición de servicios

### Modelos de Datos

#### Modelo Pago (Sistema)
```java
public class Pago {
    private Long id;
    private boolean estado;
    private Long usuarioRut;
    private Long cursoId;
}
```
- **Diferencia con Pago original**: Versión simplificada para transferencia

#### Modelo Usuario (Sistema)
```java
public class Usuario {
    private String rut;
    private String dv;
    private String nombre;
    private String email;
    private LocalDateTime fechaRegistro;
    private Long tipoUsuarioId;
}
```
- **Propósito**: Representación para agregación de datos

#### Modelo Curso (Sistema)
```java
public class Curso {
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("nombreCurso")
    private String nombreCurso;
    
    @JsonProperty("descripcionCurso")
    private String descripcionCurso;
}
```
- **Anotaciones JSON**: Para mapeo correcto desde servicios externos

#### Modelos Enriquecidos

##### PagoEnriquecido
```java
public class PagoEnriquecido {
    private Long id;
    private boolean estado;
    private Usuario usuario;        // Información completa del usuario
    private Curso curso;           // Información completa del curso
    private String nombreUsuario;  // Nombre del usuario para fácil acceso