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

     //Test : Crear usuario sin curso
    @Test
    void crearUsuario_sinCurso_exitoso() {
        when(usuarioRepository.existsByEmail("juan@example.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.crearUsuario(usuarioRequest);

        // Verificar que se creó el usuario correctamente
        // y que no se llama al microservicio de verificación de curso
        // porque no se especificó un curso en el request
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        verify(usuarioRepository).save(any(Usuario.class));
        verify(microservicioService, never()).verificarCursoExiste(anyLong());
    }


    //Test : Email duplicado
    @Test
    void crearUsuario_emailDuplicado_lanzaExcepcion() {
        // Arrange - Solo mock necesario
        when(usuarioRepository.existsByEmail("juan@example.com")).thenReturn(true);
        //Esta linea sirve para simular que el email ya existe
        // Act & Assert - Lanza excepción si el email ya existe
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearUsuario(usuarioRequest);
        });
        // assertTrue para verificar que se lanza la excepción
        // y que el mensaje contiene la validación de email duplicado
        assertTrue(exception.getMessage().contains("Ya existe un usuario con ese email"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    
    //Test : Crear usuario con curso válido
    @Test
    void crearUsuario_conCursoValido_creaUsuarioYPago() {
        // Configurar el usuarioRequest con curso y pago
        usuarioRequest.setCursoId(101L);
        usuarioRequest.setEstadoPago(true);
        // Mock de los servicios necesarios
        when(usuarioRepository.existsByEmail("juan@example.com")).thenReturn(false);
        when(microservicioService.verificarCursoExiste(101L)).thenReturn(true);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(microservicioService.crearPagoEnriquecido(anyLong(), anyString(), anyLong(), anyBoolean()))
                .thenReturn(1L);
        // Llamar al método a probar
        Usuario resultado = usuarioService.crearUsuario(usuarioRequest);
        // Verificar que se creó el usuario y el pago
        assertNotNull(resultado);
        verify(microservicioService).verificarCursoExiste(101L);
        verify(microservicioService).crearPagoEnriquecido(12345678L, "Juan Pérez", 101L, true);
    }

    // =========== OBTENER USUARIOS ===========


     //Test : Obtener todos los usuarios
    @Test
    void obtenerTodos_retornaLista() {

        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        List<Usuario> resultado = usuarioService.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getNombre());
    }

    
     //Test : Obtener por RUT válido
    @Test
    void obtenerPorRut_existe_retornaUsuario() {
        // Simular que el usuario existe en la base de datos
        when(usuarioRepository.findById(12345678L)).thenReturn(Optional.of(usuario));
        // Llamar al método a probar
        Usuario resultado = usuarioService.obtenerPorRut("12345678");
        // Verificar que se obtiene el usuario correcto
        // y que el RUT coincide con el del usuario mockeado
        // assertNotNull para verificar que el resultado no es nulo
        // assertEquals para verificar que el RUT del resultado es el esperado.
        assertNotNull(resultado);
        assertEquals(12345678L, resultado.getRut());
    }

    
     //Test : RUT inválido lanza excepción
    
    @Test
    void obtenerPorRut_rutInvalido_lanzaExcepcion() {
        //Sin mocks porque no llega a usarlos
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.obtenerPorRut("rut_invalido");
        });
        // Verificar que se lanza una excepción con el mensaje esperado
        // assertEquals para verificar que el mensaje de la excepción es el esperado
        // verify para asegurarse de que no se llama al repositorio
        // porque el RUT es inválido y no se llega a buscar en la base de datos.
        assertTrue(exception.getMessage().contains("RUT inválido"));
        assertEquals("RUT inválido", exception.getMessage());
        verify(usuarioRepository, never()).findById(anyLong());
    }

    // =========== ACTUALIZAR USUARIO ===========

    
     //Test : Actualización exitosa
    @Test
    void actualizarUsuario_datosValidos_exitoso() {
        // esta linea sirve para simular que el usuario existe en la base de datos
        when(usuarioRepository.findById(12345678L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        // Llamar al método a probar con un RUT válido
        Usuario resultado = usuarioService.actualizarUsuario("12345678", usuarioRequest);
        // Verificar que se actualiza el usuario correctamente
        // assertNotNull para verificar que el resultado no es nulo
        // verify para asegurarse de que se llama al método save del repositorio
        // y que se guarda el usuario actualizado.
        assertNotNull(resultado);
        verify(usuarioRepository).save(any(Usuario.class));
    }

     //Test : Email duplicado en actualización
     
    @Test
        void actualizarUsuario_emailDuplicado_lanzaExcepcion() {
        //esta linea sirve para simular que el usuario existe en la base de datos
        Usuario usuarioActual = new Usuario();
        usuarioActual.setRut(12345678L);
        usuarioActual.setEmail("actual@example.com"); // Email diferente al del request
        
        // Crear un request con un email que ya existe
        // Esta linea sirve para simular que el email ya existe en la base de datos
        UsuarioRequest requestNuevoEmail = new UsuarioRequest();
        requestNuevoEmail.setEmail("nuevo@example.com");

        // Simular que el usuario existe y que el email ya está en uso
        // Esta linea sirve para simular que el usuario con el email nuevo ya existe en la base de datos
        
        when(usuarioRepository.findById(12345678L)).thenReturn(Optional.of(usuarioActual));
        when(usuarioRepository.existsByEmail("nuevo@example.com")).thenReturn(true);

        //esta linea sirve para simular que se lanza una excepción
        // cuando se intenta actualizar el usuario con un email que ya existe
        // Llamar al método a probar con un RUT válido y un email que ya existe
        // assertThrows para verificar que se lanza una excepción de tipo RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.actualizarUsuario("12345678", requestNuevoEmail);
        });

        // Verificar que se lanza una excepción con el mensaje esperado
        // assertTrue para verificar que el mensaje de la excepción contiene la validación de email duplicado
        // verify para asegurarse de que no se llama al método save del repositorio
        // porque el email ya existe y no se puede actualizar el usuario.
        assertTrue(exception.getMessage().contains("Ya existe un usuario con ese email"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // =========== ELIMINAR USUARIO ===========

     //Test : Eliminación válida
    @Test
    void eliminarUsuario_existe_retornaTrue() {
        // esta linea sirve para simular que el usuario existe en la base de datos
        when(usuarioRepository.existsById(12345678L)).thenReturn(true);

        // esta linea sirve para simular que se elimina el usuario
        // Llamar al método a probar con un RUT válido
        // assertTrue para verificar que el resultado es verdadero
        boolean resultado = usuarioService.eliminarUsuario("12345678");

        // esta linea entrega el resultado de la eliminación
        // verify para asegurarse de que se llama al método deleteById del repositorio
        // con el RUT del usuario que se está eliminando.
        // assertTrue para verificar que el resultado es verdadero
        // y que se ha eliminado correctamente el usuario.
        assertTrue(resultado);
        verify(usuarioRepository).deleteById(12345678L);
    }

     //Test : Usuario no existe
    @Test
    void eliminarUsuario_noExiste_retornaFalse() {
        // esta linea sirve para simular que el usuario no existe en la base de datos
        when(usuarioRepository.existsById(99999999L)).thenReturn(false);

        // esta linea sirve para simular que se intenta eliminar un usuario que no existe
        // Llamar al método a probar con un RUT que no existe
        // assertFalse para verificar que el resultado es falso
        // y que no se ha eliminado ningún usuario.
        // verify para asegurarse de que no se llama al método  del repositorio
        boolean resultado = usuarioService.eliminarUsuario("99999999");

        // esta linea entrega el resultado de la eliminación
        // assertFalse para verificar que el resultado es falso
        // y que no se ha eliminado ningún usuario.
        // verify para asegurarse de que no se llama al método deleteById del repositorio
        // porque el usuario no existe y no se puede eliminar.
        // assertFalse para verificar que el resultado es falso
        assertFalse(resultado);
        verify(usuarioRepository, never()).deleteById(anyLong());
    }
}