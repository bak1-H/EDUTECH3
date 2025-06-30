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

    @MockBean
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
        // Mock del servicio
        when(tipoUsuarioService.obtenerTodos()).thenReturn(Arrays.asList(tipoEstudiante));

        // Petición GET
        mockMvc.perform(get("/api/tipos-usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("ESTUDIANTE"));

        verify(tipoUsuarioService, times(1)).obtenerTodos();
    }

    @Test
    void obtenerTodos_listaVacia_retornaOk() throws Exception {
        // Mock: lista vacía
        when(tipoUsuarioService.obtenerTodos()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/tipos-usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(tipoUsuarioService, times(1)).obtenerTodos();
    }

    // === OBTENER POR ID ===
    @Test
    void obtenerPorId_existente_retornaTipo() throws Exception {
        // Mock del servicio
        when(tipoUsuarioService.obtenerPorId(1L)).thenReturn(Optional.of(tipoEstudiante));

        // Petición GET por ID
        mockMvc.perform(get("/api/tipos-usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("ESTUDIANTE"))
                .andExpect(jsonPath("$.activo").value(true));

        verify(tipoUsuarioService, times(1)).obtenerPorId(1L);
    }

    @Test
    void obtenerPorId_noExiste_retorna404() throws Exception {
        // Mock: no encuentra el tipo
        when(tipoUsuarioService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tipos-usuario/999"))
                .andExpect(status().isNotFound());

        verify(tipoUsuarioService, times(1)).obtenerPorId(999L);
    }

    @Test
    void obtenerPorId_idInvalido_retorna404() throws Exception {
        // Mock: ID inválido
        when(tipoUsuarioService.obtenerPorId(0L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tipos-usuario/0"))
                .andExpect(status().isNotFound());

        verify(tipoUsuarioService, times(1)).obtenerPorId(0L);
    }
}