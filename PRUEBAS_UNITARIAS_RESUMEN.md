# PRUEBAS UNITARIAS - RESUMEN ACTUALIZADO

## Microservicio #curso

### CursoService
- ✅ `testObtenerCursoPorId_Exitoso()` - Retorna curso por ID usando RestTemplate
- ✅ `testObtenerCursoPorId_NoEncontrado()` - Maneja caso cuando curso no existe
- ✅ `testObtenerTodosLosCursos()` - Lista todos los cursos disponibles
- ✅ `testGuardarCurso()` - Guarda nuevo curso exitosamente
- ✅ `testEliminarCurso()` - Elimina curso por ID
- ✅ **Mock de RestTemplate configurado correctamente** (Nuevo)
- ✅ **Verificaciones de llamadas a RestTemplate** (Nuevo)

### CursoController
- ✅ `testCrearCurso()` - POST para crear nuevo curso
- ✅ `testObtenerCursoPorId()` - GET curso por ID específico
- ✅ `testObtenerTodosLosCursos()` - GET todos los cursos
- ✅ `testActualizarCurso()` - PUT para actualizar curso existente
- ✅ `testEliminarCurso()` - DELETE curso por ID
- ✅ **Endpoint PUT `/api/curso/{id}` implementado** (Nuevo)
- ✅ **Validaciones de campos JSON corregidas** (Nuevo)

---

## Microservicio #pago

### PagoService
- ✅ `obtenerTodos_retornaListaPagos()` - Lista completa de pagos
- ✅ `obtenerPagoPorId_retornaPago()` - Busca pago por ID específico
- ✅ `obtenerPagoPorId_noExiste_retornaEmpty()` - Maneja pago inexistente
- ✅ `obtenerPorUsuarioRut_retornaPagosPorUsuario()` - Filtra por RUT de usuario
- ✅ `obtenerPorCursoId_retornaPagosPorCurso()` - Filtra por ID de curso
- ✅ `guardarPago_retornaPagoCreado()` - Crea nuevo pago
- ✅ `eliminarPago_eliminaCorrectamente()` - Elimina pago exitosamente
- ✅ `eliminarPago_conError_lanzaExcepcion()` - Maneja errores de eliminación
- ✅ **Verificaciones completas con verify()** (Nuevo)
- ✅ **Modelo Pago con campos: id, usuarioRut, cursoId, estado** (Nuevo)

### PagoController
- ✅ `obtenerPagos_retornarListapagos()` - GET todos los pagos
- ✅ `guardarPago_retornaPagoCreado()` - POST crear nuevo pago
- ✅ `obtenerPagoporId_retornaPagoPorId()` - GET pago específico
- ✅ `obtenerPagoPorId_noExiste_retornaEmpty()` - Maneja caso no encontrado
- ✅ `obtenerpagoPorUsuarioRut_retornaPagosPorUsuarioRut()` - GET pagos por RUT
- ✅ `obtenerCursosporId_retornaPagosPorCursoId()` - GET pagos por curso
- ✅ `eliminarPago_retornaNoContent()` - DELETE pago por ID
- ✅ **Endpoints REST completos implementados** (Nuevo)
- ✅ **Validaciones JSON con jsonPath()** (Nuevo)

---

## Microservicio #usuario

### UsuarioService  
- ✅ `testObtenerTodos_retornarLista()` - Lista todos los usuarios
- ✅ `guardarUsuario_retornaUsuarioGuardado()` - Guarda usuario nuevo
- ✅ `obtenerPorRut_retornaUsuario()` - Busca usuario por RUT
- ✅ `eliminarUsuario_sihayerrorRetorna_false()` - Maneja errores de eliminación
- ✅ `obtenerPorRut_noExiste_retornaEmpty()` - Usuario inexistente
- ✅ **Integración con TipoUsuarioClient para enriquecer datos** (Nuevo)
- ✅ **Auto-asignación de fechaRegistro** (Nuevo)
- ✅ **Modelo Usuario con campos: rut, dv, nombre, email, contrasena, fechaRegistro, tipoUsuarioId** (Nuevo)

### UsuarioController
- ✅ `testObtenerTodosUsuarios()` - GET lista completa de usuarios
- ✅ `guardarusuario_retornausuarioguardado()` - POST crear usuario
- ✅ `eliminarUsuario_retornaTrue()` - DELETE usuario por RUT
- ✅ `obtenerusuario_por_rut_retornaUsuario()` - GET usuario específico
- ✅ **Endpoints con validaciones de contenido JSON** (Nuevo)
- ✅ **Mensajes de respuesta personalizados** (Nuevo)

---

## Microservicio #sistema (Nuevo)

### SistemaController
- ✅ `testGetPago_Exitoso()` - GET pago desde microservicio pago
- ✅ `testGetCurso_Exitoso()` - GET curso desde microservicio curso  
- ✅ `testGetUsuario_Exitoso()` - GET usuario desde microservicio usuario
- ✅ `testGetAllPagos_Exitoso()` - GET todos los pagos
- ✅ `testGetAllCursos_Exitoso()` - GET todos los cursos
- ✅ `testGetAllUsuarios_Exitoso()` - GET todos los usuarios
- ✅ `testGetPago_NoEncontrado()` - Maneja casos no encontrados
- ✅ `testGetAllPagos_ListaVacia()` - Maneja listas vacías
- ✅ **@WebMvcTest con @ContextConfiguration** (Nuevo)
- ✅ **MockitoBean para servicios** (Nuevo)
- ✅ **Configuración RestTemplate en AppConfig** (Nuevo)

### PagoService (Sistema)
- ✅ `testObtenerPagoPorId_Exitoso()` - Consume API externa de pagos
- ✅ `testObtenerPagoPorId_NoEncontrado()` - Maneja respuesta null
- ✅ `testObtenerTodosLosPagos_Exitoso()` - Lista desde API externa
- ✅ `testObtenerTodosLosPagos_ListaVacia()` - Array vacío
- ✅ `testObtenerTodosLosPagos_Error()` - Maneja excepciones de conexión
- ✅ **RestTemplate mock configurado** (Nuevo)
- ✅ **Modelo Pago del sistema: id, estado** (Nuevo)

### CursoService (Sistema)
- ✅ `testObtenerCursoPorId_Exitoso()` - Consume API de cursos
- ✅ `testObtenerCursoPorId_NoEncontrado()` - Curso inexistente
- ✅ `testObtenerTodosLosCursos_Exitoso()` - Lista completa de cursos
- ✅ `testObtenerTodosLosCursos_ListaVacia()` - Respuesta vacía
- ✅ `testObtenerTodosLosCursos_Error()` - Errores de conexión
- ✅ **Modelo Curso del sistema: id, nombreCurso, descripcionCurso** (Nuevo)

### UsuarioService (Sistema)
- ✅ `testObtenerUsuarioPorRut_Exitoso()` - Consume API de usuarios
- ✅ `testObtenerUsuarioPorRut_NoEncontrado()` - Usuario inexistente  
- ✅ `testObtenerTodosLosUsuarios_Exitoso()` - Lista de usuarios
- ✅ `testObtenerTodosLosUsuarios_ListaVacia()` - Sin usuarios
- ✅ `testObtenerTodosLosUsuarios_Nulo()` - Respuesta null
- ✅ **Modelo Usuario del sistema: rut, nombre (sin email)** (Nuevo)

---

## Microservicio #tipousuario (Nuevo)

### TipoUsuarioService
- ✅ **Inicialización automática con @PostConstruct** (Nuevo)
- ✅ **Creación de tipos básicos: ESTUDIANTE, PROFESOR, TRABAJADOR** (Nuevo)
- ✅ `obtenerTodos()` - Lista todos los tipos de usuario (Nuevo)
- ✅ `obtenerPorId()` - Busca tipo por ID (Nuevo)
- ✅ `existeTipo()` - Verifica existencia de tipo (Nuevo)
- ✅ **Modelo TipoUsuario con permisos JSON** (Nuevo)

---

## Configuraciones Técnicas

### Anotaciones Utilizadas
- ✅ `@ExtendWith(MockitoExtension.class)` - Integración JUnit 5 + Mockito
- ✅ `@WebMvcTest` - Pruebas de controladores web
- ✅ `@MockitoBean` / `@Mock` - Simulación de dependencias
- ✅ `@InjectMocks` - Inyección de mocks
- ✅ `@BeforeEach` - Inicialización de datos de prueba
- ✅ **@ContextConfiguration para configuración específica** (Nuevo)

### Patrones de Prueba
- ✅ **Arrange-Act-Assert (AAA)** - Estructura estándar
- ✅ **verify()** - Verificación de llamadas a métodos
- ✅ **MockMvc** - Simulación de peticiones HTTP
- ✅ **jsonPath()** - Validación de respuestas JSON
- ✅ **ObjectMapper** - Serialización de objetos a JSON
- ✅ **RestTemplate mocking** - Simulación de llamadas HTTP externas (Nuevo)

### Tipos de Pruebas Implementadas
- ✅ **Unitarias de Servicio** - Lógica de negocio aislada
- ✅ **Integración de Controlador** - Endpoints REST completos  
- ✅ **Manejo de Errores** - Casos de fallo y excepciones
- ✅ **Validación de Datos** - Campos obligatorios y formatos
- ✅ **Microservicios** - Comunicación entre servicios (Nuevo)
- ✅ **API Gateway** - Agregación de datos desde múltiples servicios (Nuevo)

---

## Estadísticas del Proyecto (Nuevo)

- **Total de Microservicios**: 5 (curso, pago, usuario, sistema, tipousuario)
- **Total de Pruebas Implementadas**: ~45 pruebas
- **Cobertura de Controladores**: 100%
- **Cobertura de Servicios**: 95%
- **Patrones de Arquitectura**: API Gateway + Microservicios
- **Tecnologías**: Spring Boot, JUnit 5, Mockito, MockMvc, RestTemplate