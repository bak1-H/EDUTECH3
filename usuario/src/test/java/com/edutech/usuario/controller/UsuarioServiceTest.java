package com.edutech.usuario.controller;
import com.edutech.usuario.model.UsuarioRequest;
import com.edutech.usuario.model.Usuario;
import com.edutech.usuario.repository.UsuarioRepository;
import com.edutech.usuario.service.MicroservicioService;
import com.edutech.usuario.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests esenciales para UsuarioService
 * Enfocado en validaciones críticas y flujos principales
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MicroservicioService microservicioService;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioRequest usuarioRequest;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setRut(12345678L);
        usuario.setDv("9");
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan@example.com");
        usuario.setTipoUsuarioId(1L);
        usuario.setFechaRegistro(LocalDateTime.now());

        usuarioRequest = new UsuarioRequest();
        usuarioRequest.setRut(12345678L);
        usuarioRequest.setDv("9");
        usuarioRequest.setNombre("Juan Pérez");
        usuarioRequest.setEmail("juan@example.com");
        usuarioRequest.setTipoUsuarioId(1L);
    }

    // =========== CREAR USUARIO ===========


     //Test: Crear usuario sin curso (flujo básico)
     
    @Test
    void crearUsuario_sinCurso_exitoso() {
        // Arrange
        when(usuarioRepository.existsByEmail("juan@example.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        Usuario resultado = usuarioService.crearUsuario(usuarioRequest);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        verify(usuarioRepository).save(any(Usuario.class));
        // No debe llamar servicios de curso/pago
        verify(microservicioService, never()).verificarCursoExiste(anyLong());
    }


     //Test : Email duplicado (validación principal)

    @Test
    void crearUsuario_emailDuplicado_lanzaExcepcion() {
        // Arrange
        when(usuarioRepository.existsByEmail("juan@example.com")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuarioRequest);
        });

        assertTrue(exception.getMessage().contains("Ya existe un usuario con ese email"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    

    // =========== OBTENER USUARIOS ===========

    //Test: Obtener todos los usuarios

    @Test
    void obtenerTodos_retornaLista() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));
        List<Usuario> resultado = usuarioService.obtenerTodos();
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getNombre());
    }

     //Test exitoso: Obtener por RUT válido
    @Test
    void obtenerPorRut_existe_retornaUsuario() {
        when(usuarioRepository.findById(12345678L)).thenReturn(Optional.of(usuario));
        Usuario resultado = usuarioService.obtenerPorRut("12345678");
        assertNotNull(resultado);
        assertEquals(12345678L, resultado.getRut());
    }


     //Test: RUT inválido lanza excepción
    @Test
    void obtenerPorRut_rutInvalido_lanzaExcepcion() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.obtenerPorRut("rut_invalido");
        });
        assertEquals("RUT inválido", exception.getMessage());
        verify(usuarioRepository, never()).findById(anyLong());
    }


     // Test : Email duplicado en actualización
    @Test
    void actualizarUsuario_emailDuplicado_lanzaExcepcion() {
        usuario.setEmail("actual@example.com"); // Email actual diferente
        usuarioRequest.setEmail("nuevo@example.com"); // Nuevo email
        when(usuarioRepository.findById(12345678L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.existsByEmail("nuevo@example.com")).thenReturn(true);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.actualizarUsuario("12345678", usuarioRequest);
        });
        assertTrue(exception.getMessage().contains("Ya existe un usuario con ese email"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // =========== ELIMINAR USUARIO ===========

    /**
     * Test exitoso: Eliminación válida
     */
    @Test
    void eliminarUsuario_existe_retornaTrue() {
        when(usuarioRepository.existsById(12345678L)).thenReturn(true);
        boolean resultado = usuarioService.eliminarUsuario("12345678");
        assertTrue(resultado);
        verify(usuarioRepository).deleteById(12345678L);
    }

    /**
     * Test  Usuario no existe
     */
    @Test
    void eliminarUsuario_noExiste_retornaFalse() {
        when(usuarioRepository.existsById(99999999L)).thenReturn(false);
        boolean resultado = usuarioService.eliminarUsuario("99999999");
        assertFalse(resultado);
        verify(usuarioRepository, never()).deleteById(anyLong());
    }
}