package com.edutech.sistema.controller;

import com.edutech.sistema.model.Pago;
import com.edutech.sistema.model.Curso;
import com.edutech.sistema.model.Usuario;
import com.edutech.sistema.service.PagoService;
import com.edutech.sistema.service.CursoService;
import com.edutech.sistema.service.UsuarioService;
import com.edutech.sistema.service.EnriquecimientoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SistemaController.class)
class SistemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagoService pagoService;

    @MockBean
    private CursoService cursoService;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private EnriquecimientoService enriquecimientoService;

    private Pago pagoEjemplo;
    private Curso cursoEjemplo;
    private Usuario usuarioEjemplo;

    @BeforeEach
    void setUp() {
        // Usuario básico
        usuarioEjemplo = new Usuario();
        usuarioEjemplo.setRut("12345678");
        usuarioEjemplo.setDv("9");
        usuarioEjemplo.setNombre("Juan Pérez");
        usuarioEjemplo.setEmail("juan@email.com");
        usuarioEjemplo.setFechaRegistro(LocalDateTime.now());
        usuarioEjemplo.setTipoUsuarioId(1L);

        // Curso básico
        cursoEjemplo = new Curso();
        cursoEjemplo.setId(101L);
        cursoEjemplo.setNombreCurso("Curso de Java");
        cursoEjemplo.setDescripcionCurso("Curso completo de Java");

        // Pago básico
        pagoEjemplo = new Pago();
        pagoEjemplo.setId(1L);
        pagoEjemplo.setEstado(true);
        pagoEjemplo.setUsuarioRut(12345678L);
        pagoEjemplo.setCursoId(101L);
    }

    // === TESTS ESENCIALES ÚNICAMENTE ===

    @Test
    void testGetAllPagos_OK() throws Exception {
        when(pagoService.obtenerTodosLosPagos()).thenReturn(Arrays.asList(pagoEjemplo));
        when(enriquecimientoService.enriquecerPagos(any())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/sistema/pagos"))
                .andExpect(status().isOk());

        verify(pagoService, times(1)).obtenerTodosLosPagos();
    }

    @Test
    void testGetAllCursos_OK() throws Exception {
        when(cursoService.obtenerTodosLosCursos()).thenReturn(Arrays.asList(cursoEjemplo));
        when(enriquecimientoService.enriquecerCursos(any())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/sistema/cursos"))
                .andExpect(status().isOk());

        verify(cursoService, times(1)).obtenerTodosLosCursos();
    }

    @Test
    void testGetAllUsuarios_OK() throws Exception {
        when(usuarioService.obtenerTodosLosUsuarios()).thenReturn(Arrays.asList(usuarioEjemplo));
        when(enriquecimientoService.enriquecerUsuarios(any())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/sistema/usuarios"))
                .andExpect(status().isOk());

        verify(usuarioService, times(1)).obtenerTodosLosUsuarios();
    }
}