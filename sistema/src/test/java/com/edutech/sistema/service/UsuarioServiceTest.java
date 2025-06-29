package com.edutech.sistema.service;
import com.edutech.sistema.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UsuarioService usuarioService;

    private com.edutech.sistema.model.Usuario usuarioEjemplo;

    @BeforeEach
    void setUp() {
        usuarioEjemplo = new Usuario();
        usuarioEjemplo.setRut("12345678-9");
        usuarioEjemplo.setNombre("Juan Pérez");
    }

    @Test
    void testObtenerUsuarioPorRut_Exitoso() {
        // Arrange
        String rut = "12345678-9";
        when(restTemplate.getForObject(anyString(), eq(Usuario.class)))
            .thenReturn(usuarioEjemplo);

        // Act
        Usuario resultado = usuarioService.obtenerUsuarioPorRut(rut);

        // Assert
        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRut());
        assertEquals("Juan Pérez", resultado.getNombre());
    }

    @Test
    void testObtenerUsuarioPorRut_NoEncontrado() {
        // Arrange
        String rut = "99999999-9";
        when(restTemplate.getForObject(anyString(), eq(Usuario.class)))
            .thenReturn(null);

        // Act
        Usuario resultado = usuarioService.obtenerUsuarioPorRut(rut);

        // Assert
        assertNull(resultado);
    }

    @Test
    void testObtenerTodosLosUsuarios_Exitoso() {
        // Arrange
        Usuario usuario2 = new Usuario();
        usuario2.setRut("87654321-0");
        usuario2.setNombre("María González");
        
        Usuario[] usuariosArray = {usuarioEjemplo, usuario2};
        when(restTemplate.getForObject(anyString(), eq(Usuario[].class)))
            .thenReturn(usuariosArray);

        // Act
        List<Usuario> resultado = usuarioService.obtenerTodosLosUsuarios();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getNombre());
        assertEquals("María González", resultado.get(1).getNombre());
    }

    @Test
    void testObtenerTodosLosUsuarios_ListaVacia() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(Usuario[].class)))
            .thenReturn(new Usuario[0]);

        // Act
        List<Usuario> resultado = usuarioService.obtenerTodosLosUsuarios();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testObtenerTodosLosUsuarios_Nulo() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(Usuario[].class)))
            .thenReturn(null);

        // Act
        List<Usuario> resultado = usuarioService.obtenerTodosLosUsuarios();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}