package com.edutech.usuario.services;

import com.edutech.usuario.model.Usuario;
import com.edutech.usuario.repository.repository_usuario;
import com.edutech.usuario.service.UsuarioService;
import org.mockito.Mock;
import org.mockito.InjectMocks;

import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class) 
public class TestServices {



    //la funcion de @Mock es simular el comportamiento de un objeto
    //en este caso, simula el repositorio de usuarios
@Mock
private repository_usuario usuarioRepository;

//@InjectMocks se usa para inyectar los mocks en la clase que se va a probar
//en este caso, se inyecta el repositorio simulado en el servicio de usuarios
@InjectMocks
private UsuarioService usuarioService;

private Usuario usuario1;
private Usuario usuario2;


    @BeforeEach //Esto sirve para inicializar los datos antes de cada prueba
    void setUp(){

        usuario1 = new Usuario();
        usuario1.setRut(12345678L);
        usuario1.setDv("9");
        usuario1.setNombre("Juan");
        usuario1.setEmail("juanperez@gmail.com");
        usuario1.setContrasena("naruto456");
        usuario1.setFechaRegistro(null); //Se deja nulo por que el servicio lo asigna automáticamente
        usuario1.setTipoUsuarioId(1L); //Asumiendo que el tipo de usuario es 1 


        usuario2 = new Usuario();
        usuario2.setRut(87654321L);
        usuario2.setDv("8");
        usuario2.setNombre("Maria");
        usuario2.setEmail("mariaperez@gmail.com");
        usuario2.setContrasena("naruto123");
        usuario2.setFechaRegistro(null); //Se deja nulo por que el servicio lo asigna automáticamente
        usuario2.setTipoUsuarioId(2L); //Asumiendo que el tipo de usuario es 2
    }
    


//Esta prueba verifica que el método obtenerTodos del servicio
//retorne una lista de usuarios cuando se le llama sin parámetros.
//En este caso, se simula el comportamiento del repositorio para que devuelva una lista
//de usuarios predefinida, y luego se verifica que el servicio retorne esa lista.
    @Test 
    void testObtenerTodos_retornarLista() {
        // Arrange
        ArrayList<Usuario> listarusuariostest = new ArrayList<>(Arrays.asList(usuario1, usuario2));
        when(usuarioRepository.findAll()).thenReturn(listarusuariostest);

        // Act
        ArrayList<Usuario> resultado = (ArrayList<Usuario>) usuarioService.obtenerTodos();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
        assertEquals("Maria", resultado.get(1).getNombre());
        verify(usuarioRepository, times(1)).findAll();
    }



//Esta prueba verifica que el método obtenerTodos del servicio
//retorne una lista de usuarios cuando se le llama sin parámetros.

    @Test  
    void guardarUsuario_retornaUsuarioGuardado() {
        // Arrange - Mock el comportamiento del repositorio
        when(usuarioRepository.save(usuario1)).thenReturn(usuario1);
        
        // Act - Llamar al método del servicio
        Usuario resultadoGuardado = usuarioService.guardarUsuario(usuario1);

        // Assert - Verificar el resultado
        assertNotNull(resultadoGuardado);
        assertEquals("Juan", resultadoGuardado.getNombre());
        assertEquals(12345678L, resultadoGuardado.getRut());
        
        // Verify - Verificar que se llamó al repositorio
        verify(usuarioRepository, times(1)).save(usuario1);
    }

//Esta prueba verifica que el método guardarUsuario del servicio
//retorne un usuario guardado cuando se le pasa un usuario válido.
    @Test
    void obtenerPorRut_retornaUsuario() {
        // Arrange
        when(usuarioRepository.findById(12345678L)).thenReturn(Optional.of(usuario1));
        
        // Act
        Optional<Usuario> resultado = usuarioService.obtenerPorRut(12345678L);
        
        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Juan", resultado.get().getNombre());
        verify(usuarioRepository, times(1)).findById(12345678L);
    }


//Esta Prueba verifica que el método obtenerPorRut del servicio

    @Test
    void eliminarUsuario_sihayerrorRetorna_false() {
        // Arrange - Configurar el mock para lanzar excepción
        doThrow(new RuntimeException("Error al eliminar el usuario"))
            .when(usuarioRepository).deleteById(1L);

        // Act & Assert - Verificar que se lanza la excepción
        assertThrows(RuntimeException.class, () -> {
            usuarioService.eliminarUsuario(1L);
        });

        // Verify - Verificar que se llamó al método
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

        @Test
    void obtenerPorRut_noExiste_retornaEmpty() {
        // Arrange
        when(usuarioRepository.findById(99999999L)).thenReturn(Optional.empty());
        
        // Act
        Optional<Usuario> resultado = usuarioService.obtenerPorRut(99999999L);
        
        // Assert
        assertFalse(resultado.isPresent());
        verify(usuarioRepository, times(1)).findById(99999999L);
    }
}




