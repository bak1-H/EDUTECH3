package com.edutech.sistema.controller;

import com.edutech.sistema.model.controller.SistemaController;
import com.edutech.sistema.model.Pago;
import com.edutech.sistema.model.Curso;
import com.edutech.sistema.model.Usuario;
import com.edutech.sistema.service.PagoService;
import com.edutech.sistema.service.CursoService;
import com.edutech.sistema.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SistemaController.class)

// Esta anotación configura un contexto de prueba para el controlador SistemaController
// y permite inyectar dependencias como MockMvc para realizar pruebas de integración.
// WebMvcTest solo carga los componentes relacionados con la capa web
// y no carga toda la aplicación, lo que hace que las pruebas sean más rápidas y enfocadas.
// También se usa MockitoBean para simular los servicios que el controlador utiliza.
@ContextConfiguration(classes = SistemaController.class)
class SistemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PagoService pagoService;

    @MockitoBean
    private CursoService cursoService;

    @MockitoBean
    private UsuarioService usuarioService;

    private Pago pagoEjemplo;
    private Curso cursoEjemplo;
    private Usuario usuarioEjemplo;

    @BeforeEach
    void setUp() {
        // Pago: solo tiene id y estado
        pagoEjemplo = new Pago();
        pagoEjemplo.setId(1L);
        pagoEjemplo.setEstado(true);

        // Curso: id, nombreCurso, descripcionCurso
        cursoEjemplo = new Curso();
        cursoEjemplo.setId(1L);
        cursoEjemplo.setNombreCurso("Curso de Java");
        cursoEjemplo.setDescripcionCurso("Curso completo de Java");

        // Usuario: solo rut y nombre (NO tiene email)
        usuarioEjemplo = new Usuario();
        usuarioEjemplo.setRut("12345678-9");
        usuarioEjemplo.setNombre("Juan Pérez");
    }

    // Pruebas para obtener un pago por ID
    // Verifica que el controlador retorne un pago con los campos correctos
    // y que el servicio correspondiente sea llamado una vez con el ID correcto.
    // También maneja el caso donde el pago no se encuentra, retornando un estado 200 OK.
    @Test
    void testGetPago_Exitoso() throws Exception {

        Long pagoId = 1L;
        when(pagoService.obtenerPagoPorId(pagoId)).thenReturn(pagoEjemplo);
        mockMvc.perform(get("/api/sistema/pagos/{id}", pagoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.estado").value(true));
        verify(pagoService, times(1)).obtenerPagoPorId(pagoId);
    }

    // Pruebas para obtener un curso por ID
    // Verifica que el controlador retorne un curso con los campos correctos
    // y que el servicio correspondiente sea llamado una vez con el ID correcto.
    // También maneja el caso donde el curso no se encuentra, retornando un estado 200 OK.
    @Test
    void testGetCurso_Exitoso() throws Exception {
        Long cursoId = 1L;
        when(cursoService.obtenerCursoPorId(cursoId)).thenReturn(cursoEjemplo);
        mockMvc.perform(get("/api/sistema/cursos/{id}", cursoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombreCurso").value("Curso de Java"))
                .andExpect(jsonPath("$.descripcionCurso").value("Curso completo de Java"));
        verify(cursoService, times(1)).obtenerCursoPorId(cursoId);
    }


    // Pruebas para obtener un usuario por RUT
    // Verifica que el controlador retorne un usuario con los campos correctos
    // (solo rut y nombre, sin email) y que el servicio correspondiente sea llamado una vez con el RUT correcto.
    // También maneja el caso donde el usuario no se encuentra, retornando un estado 200 OK.
    @Test
    void testGetUsuario_Exitoso() throws Exception {
        String rut = "12345678-9";
        when(usuarioService.obtenerUsuarioPorRut(rut)).thenReturn(usuarioEjemplo);
        mockMvc.perform(get("/api/sistema/usuarios/{rut}", rut))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));

        verify(usuarioService, times(1)).obtenerUsuarioPorRut(rut);
    }

    // Pruebas para obtener todos los pagos exitoso
    // Verifica que el controlador retorne una lista de pagos
    // campos correctos y que los servicios correspondientes sean llamados una vez.
    // También maneja el caso donde las listas están vacías, retornando un estado

    @Test
    void testGetAllPagos_Exitoso() throws Exception {
        List<Pago> pagos = Arrays.asList(pagoEjemplo);
        when(pagoService.obtenerTodosLosPagos()).thenReturn(pagos);
        mockMvc.perform(get("/api/sistema/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].estado").value(true));
        verify(pagoService, times(1)).obtenerTodosLosPagos();
    }

    //Pruebas para obtener todos los cursos
    // Verifica que el controlador retorne una lista de cursos con los campos correctos
    // y que el servicio correspondiente sea llamado una vez.
    // También maneja el caso donde la lista de cursos está vacía, retornando un estado 200 OK.

    @Test
    void testGetAllCursos_Exitoso() throws Exception {
        // Arrange
        List<Curso> cursos = Arrays.asList(cursoEjemplo);
        when(cursoService.obtenerTodosLosCursos()).thenReturn(cursos);

        // Act & Assert - Campos correctos de Curso
        mockMvc.perform(get("/api/sistema/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombreCurso").value("Curso de Java"));

        verify(cursoService, times(1)).obtenerTodosLosCursos();
    }

    // Pruebas para obtener todos los usuarios
    // Verifica que el controlador retorne una lista de usuarios con los campos correctos
    // (solo rut y nombre, sin email) y que el servicio correspondiente sea llamado una vez.
    // También maneja el caso donde la lista de usuarios está vacía, retornando un estado 200 OK.

    @Test
    void testGetAllUsuarios_Exitoso() throws Exception {
        List<Usuario> usuarios = Arrays.asList(usuarioEjemplo);
        when(usuarioService.obtenerTodosLosUsuarios()).thenReturn(usuarios);
        mockMvc.perform(get("/api/sistema/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].rut").value("12345678-9"))
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"));

        verify(usuarioService, times(1)).obtenerTodosLosUsuarios();
    }

    // Pruebas para manejar casos donde no se encuentra un pago por Id
    // Verifica que el controlador retorne un estado 200 OK cuando no se encuentra un
    //Sin el pago ID no se puede obtener informacion relacionada al pago.

    @Test
    void testGetPago_NoEncontrado() throws Exception {
        // Arrange
        Long pagoId = 999L;
        when(pagoService.obtenerPagoPorId(pagoId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/sistema/pagos/{id}", pagoId))
                .andExpect(status().isOk());

        verify(pagoService, times(1)).obtenerPagoPorId(pagoId);
    }

    //Prueba para obtener todos los pagos cuando la lista está vacía
    // Verifica que el controlador retorne un estado 200 OK y una lista vacía
    @Test
    void testGetAllPagos_ListaVacia() throws Exception {
        // Arrange
        when(pagoService.obtenerTodosLosPagos()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/sistema/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(pagoService, times(1)).obtenerTodosLosPagos();
    }
}