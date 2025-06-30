package services;
import com.duoc.Fullstack3.models.TipoUsuario;
import com.duoc.Fullstack3.repository.TipoUsuarioRepository;
import com.duoc.Fullstack3.services.TipoUsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoUsuarioServiceTest {

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @InjectMocks
    private TipoUsuarioService tipoUsuarioService;

    private TipoUsuario tipoEstudiante;

    @BeforeEach
    void setUp() {
        // Crear tipo de usuario para pruebas
        tipoEstudiante = new TipoUsuario();
        tipoEstudiante.setId(1L);
        tipoEstudiante.setNombre("Estudiante");
        tipoEstudiante.setDescripcion("Usuario tipo estudiante");
    }

    // === OBTENER TODOS ===
    @Test
    void obtenerTodos_retornaLista() {
        // Mock: repository devuelve lista
        when(tipoUsuarioRepository.findAll()).thenReturn(Arrays.asList(tipoEstudiante));

        // Ejecutar y verificar
        List<TipoUsuario> resultado = tipoUsuarioService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Estudiante", resultado.get(0).getNombre());
        verify(tipoUsuarioRepository, times(1)).findAll();
    }

    // === OBTENER POR ID ===
    @Test
    void obtenerPorId_existe_retornaTipo() {
        // Mock: repository encuentra el tipo
        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoEstudiante));

        // Ejecutar y verificar
        Optional<TipoUsuario> resultado = tipoUsuarioService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Estudiante", resultado.get().getNombre());
        verify(tipoUsuarioRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_noExiste_retornaVacio() {
        // Mock: repository no encuentra el tipo
        when(tipoUsuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // Ejecutar y verificar
        Optional<TipoUsuario> resultado = tipoUsuarioService.obtenerPorId(999L);

        assertFalse(resultado.isPresent());
        verify(tipoUsuarioRepository, times(1)).findById(999L);
    }

    // === VERIFICAR EXISTENCIA ===
    @Test
    void existeTipo_existe_retornaTrue() {
        // Mock: repository confirma que existe
        when(tipoUsuarioRepository.existsById(1L)).thenReturn(true);

        // Ejecutar y verificar
        boolean resultado = tipoUsuarioService.existeTipo(1L);

        assertTrue(resultado);
        verify(tipoUsuarioRepository, times(1)).existsById(1L);
    }

    @Test
    void existeTipo_noExiste_retornaFalse() {
        // Mock: repository confirma que no existe
        when(tipoUsuarioRepository.existsById(999L)).thenReturn(false);

        // Ejecutar y verificar
        boolean resultado = tipoUsuarioService.existeTipo(999L);

        assertFalse(resultado);
        verify(tipoUsuarioRepository, times(1)).existsById(999L);
    }
}