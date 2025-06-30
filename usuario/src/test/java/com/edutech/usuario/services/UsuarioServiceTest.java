package com.edutech.usuario.services;

import com.edutech.usuario.model.Usuario;
import com.edutech.usuario.model.UsuarioRequest;
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

    /**
     * Test principal: Crear usuario sin curso (flujo básico)
     */
    @Test
    void crearUsuario_sinCurso_exitoso() {
        // Arrange - Solo los mocks que SE VAN A USAR
        when(usuarioRepository.existsByEmail("juan@example.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        Usuario resultado = usuarioService.crearUsuario(usuarioRequest);

        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        verify(usuarioRepository).save(any(Usuario.class));
        verify(microservicioService, never()).verificarCursoExiste(anyLong());
    }

    /**
     * Test crítico: Email duplicado (validación principal)
     */
    @Test
    void crearUsuario_emailDuplicado_lanzaExcepcion() {
        // Arrange - Solo mock necesario
        when(usuarioRepository.existsByEmail("juan@example.com")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuarioRequest);
        });

        assertTrue(exception.getMessage().contains("Ya existe un usuario con ese email"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    /**
     * Test integración: Crear usuario con curso válido
     */
    @Test
    void crearUsuario_conCursoValido_creaUsuarioYPago() {
        // Arrange
        usuarioRequest.setCursoId(101L);
        usuarioRequest.setEstadoPago(true);
        
        when(usuarioRepository.existsByEmail("juan@example.com")).thenReturn(false);
        when(microservicioService.verificarCursoExiste(101L)).thenReturn(true);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(microservicioService.crearPagoEnriquecido(anyLong(), anyString(), anyLong(), anyBoolean()))
                .thenReturn(1L);

        // Act
        Usuario resultado = usuarioService.crearUsuario(usuarioRequest);

        // Assert
        assertNotNull(resultado);
        verify(microservicioService).verificarCursoExiste(101L);
        verify(microservicioService).crearPagoEnriquecido(12345678L, "Juan Pérez", 101L, true);
    }

    // =========== OBTENER USUARIOS ===========

    /**
     * Test básico: Obtener todos los usuarios
     */
    @Test
    void obtenerTodos_retornaLista() {
        // Arrange
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        // Act
        List<Usuario> resultado = usuarioService.obtenerTodos();

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getNombre());
    }

    /**
     * Test exitoso: Obtener por RUT válido
     */
    @Test
    void obtenerPorRut_existe_retornaUsuario() {
        // Arrange
        when(usuarioRepository.findById(12345678L)).thenReturn(Optional.of(usuario));

        // Act
        Usuario resultado = usuarioService.obtenerPorRut("12345678");

        // Assert
        assertNotNull(resultado);
        assertEquals(12345678L, resultado.getRut());
    }

    /**
     * Test crítico: RUT inválido lanza excepción
     */
    @Test
    void obtenerPorRut_rutInvalido_lanzaExcepcion() {
        // Act & Assert - Sin mocks porque no llega a usarlos
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.obtenerPorRut("rut_invalido");
        });

        assertEquals("RUT inválido", exception.getMessage());
        verify(usuarioRepository, never()).findById(anyLong());
    }

    // =========== ACTUALIZAR USUARIO ===========

    /**
     * Test principal: Actualización exitosa
     * CORREGIDO: Solo mocks necesarios
     */
    @Test
    void actualizarUsuario_datosValidos_exitoso() {
        // Arrange - Solo configurar lo que SE USA realmente
        when(usuarioRepository.findById(12345678L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        // ELIMINADO: existsByEmail porque con el mismo email no se valida

        // Act
        Usuario resultado = usuarioService.actualizarUsuario("12345678", usuarioRequest);

        // Assert
        assertNotNull(resultado);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    /**
     * Test crítico: Email duplicado en actualización
     */
    @Test
    void actualizarUsuario_emailDuplicado_lanzaExcepcion() {
        // Arrange
        Usuario usuarioActual = new Usuario();
        usuarioActual.setRut(12345678L);
        usuarioActual.setEmail("actual@example.com"); // Email diferente al del request
        
        UsuarioRequest requestNuevoEmail = new UsuarioRequest();
        requestNuevoEmail.setEmail("nuevo@example.com");
        
        when(usuarioRepository.findById(12345678L)).thenReturn(Optional.of(usuarioActual));
        when(usuarioRepository.existsByEmail("nuevo@example.com")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.actualizarUsuario("12345678", requestNuevoEmail);
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
        // Arrange
        when(usuarioRepository.existsById(12345678L)).thenReturn(true);

        // Act
        boolean resultado = usuarioService.eliminarUsuario("12345678");

        // Assert
        assertTrue(resultado);
        verify(usuarioRepository).deleteById(12345678L);
    }

    /**
     * Test edge case: Usuario no existe
     */
    @Test
    void eliminarUsuario_noExiste_retornaFalse() {
        // Arrange
        when(usuarioRepository.existsById(99999999L)).thenReturn(false);

        // Act
        boolean resultado = usuarioService.eliminarUsuario("99999999");

        // Assert
        assertFalse(resultado);
        verify(usuarioRepository, never()).deleteById(anyLong());
    }
}