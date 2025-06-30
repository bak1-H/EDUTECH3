package controller;

import com.duoc.Fullstack3.controllers.TipoUsuarioController;
import com.duoc.Fullstack3.models.TipoUsuario;
import com.duoc.Fullstack3.services.TipoUsuarioService;
import com.duoc.Fullstack3.Fullstack3Application; // ← Importar la clase principal
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TipoUsuarioController.class)
@ContextConfiguration(classes = Fullstack3Application.class) // ← Especificar la clase principal
class TipoUsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TipoUsuarioService tipoUsuarioService;

    private TipoUsuario tipoEstudiante;

    @BeforeEach
    void setUp() {
        // Crear datos de prueba
        tipoEstudiante = new TipoUsuario();
        tipoEstudiante.setId(1L);
        tipoEstudiante.setNombre("ESTUDIANTE");
        tipoEstudiante.setDescripcion("Estudiante del sistema educativo");
        tipoEstudiante.setPermisos("{\"acceso_basico\":true}");
        tipoEstudiante.setActivo(true);
    }

    // === OBTENER TODOS ===
    @Test
    void obtenerTodos_retornaListaCompleta() throws Exception {
        // esta linea sirve para simular que el servicio devuelve una lista con un tipo de usuario
        // Mock: lista con un tipo de usuario
        when(tipoUsuarioService.obtenerTodos()).thenReturn(Arrays.asList(tipoEstudiante));

        // Petición GET
        mockMvc.perform(get("/api/tipos-usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("ESTUDIANTE"));

        // Verificar que se llama al servicio una vez
        // verify para asegurarse de que se llama al método obtenerTodos del servicio
        verify(tipoUsuarioService, times(1)).obtenerTodos();
    }

    @Test
    void obtenerTodos_listaVacia_retornaOk() throws Exception {
        // Mock: lista vacía
        when(tipoUsuarioService.obtenerTodos()).thenReturn(Arrays.asList());

        // Petición GET
        // esta linea sirve para simular que el servicio devuelve una lista vacía
        // MockMvc realiza una petición GET a la URL /api/tipos-usuario
        mockMvc.perform(get("/api/tipos-usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        // Verificar que se llama al servicio una vez
        // verify para asegurarse de que se llama al método obtenerTodos del servicio
        verify(tipoUsuarioService, times(1)).obtenerTodos();
    }

    // === OBTENER POR ID ===
    @Test
    void obtenerPorId_existente_retornaTipo() throws Exception {
        // esta linea sirve para simular que el servicio devuelve un tipo de usuario por ID
        // Mock: tipo de usuario existente
        when(tipoUsuarioService.obtenerPorId(1L)).thenReturn(Optional.of(tipoEstudiante));

        // Petición GET por ID
        // MockMvc realiza una petición GET a la URL /api/tipos-usuario/1
        mockMvc.perform(get("/api/tipos-usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("ESTUDIANTE"))
                .andExpect(jsonPath("$.activo").value(true));

        verify(tipoUsuarioService, times(1)).obtenerPorId(1L);
    }

    @Test
    void obtenerPorId_noExiste_retorna404() throws Exception {
        // esta linea sirve para simular que el servicio no encuentra un tipo de usuario por ID
        // Mock: tipo de usuario no existente
        when(tipoUsuarioService.obtenerPorId(999L)).thenReturn(Optional.empty());

        // Petición GET por ID
        // MockMvc realiza una petición GET a la URL /api/tipos-usuario/999
        // y espera un estado 404 Not Found
        mockMvc.perform(get("/api/tipos-usuario/999"))
                .andExpect(status().isNotFound());

        // Verificar que se llama al servicio una vez
        // verify para asegurarse de que se llama al método obtenerPorId del servicio
        verify(tipoUsuarioService, times(1)).obtenerPorId(999L);
    }

}