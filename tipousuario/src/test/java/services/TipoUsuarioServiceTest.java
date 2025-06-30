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
        // esta linea sirve para simular que el repository devuelve una lista con un tipo de usuario
        // Mock: repository devuelve una lista con un tipo de usuario
        when(tipoUsuarioRepository.findAll()).thenReturn(Arrays.asList(tipoEstudiante));

        // esta linea entrega el resultado de la obtención
        List<TipoUsuario> resultado = tipoUsuarioService.obtenerTodos();

        // assertNotNull para verificar que el resultado no es nulo
        assertNotNull(resultado);
        // assertEquals para verificar que el tamaño de la lista es 1
        assertEquals(1, resultado.size());
        // assertEquals para verificar que el nombre del tipo de usuario es "Estudiante"
        assertEquals("Estudiante", resultado.get(0).getNombre());
        // verify para asegurarse de que se llama al método findAll del repository
        // y que se ha ejecutado una vez
        verify(tipoUsuarioRepository, times(1)).findAll();
    }

    // === OBTENER POR ID ===
    @Test
    void obtenerPorId_existe_retornaTipo() {
        // esta linea sirve para simular que el repository encuentra un tipo de usuario por ID
        // Mock: repository encuentra el tipo de usuario por ID
        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoEstudiante));

        // esta linea entrega el resultado de la obtención
        // Ejecutar y verificar
        Optional<TipoUsuario> resultado = tipoUsuarioService.obtenerPorId(1L);

        // assertTrue para verificar que el resultado es un Optional con valor
        // y que contiene el tipo de usuario esperado
        assertTrue(resultado.isPresent());
        // assertEquals para verificar que el nombre del tipo de usuario es "Estudiante"
        // y que se ha ejecutado una vez
        assertEquals("Estudiante", resultado.get().getNombre());
        // verify para asegurarse de que se llama al método findById del repository
        verify(tipoUsuarioRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_noExiste_retornaVacio() {
        // esta linea sirve para simular que el repository no encuentra un tipo de usuario por ID
        // Mock: repository no encuentra el tipo de usuario por ID
        when(tipoUsuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // esta linea entrega el resultado de la obtención
        Optional<TipoUsuario> resultado = tipoUsuarioService.obtenerPorId(999L);
        // assertFalse para verificar que el resultado es un Optional vacío
        // y que no contiene ningún tipo de usuario
        assertFalse(resultado.isPresent());
        // verify para asegurarse de que se llama al método findById del repository
        // y que se ha ejecutado una vez
        verify(tipoUsuarioRepository, times(1)).findById(999L);
    }

    // === VERIFICAR EXISTENCIA ===
    @Test
    void existeTipo_existe_retornaTrue() {
        // esta linea sirve para simular que el repository confirma que existe un tipo de usuario
        // Mock: repository confirma que existe el tipo de usuario con ID 1
        when(tipoUsuarioRepository.existsById(1L)).thenReturn(true);

        //esta linea entrega el resultado de la verificación
        boolean resultado = tipoUsuarioService.existeTipo(1L);
        // assertTrue para verificar que el resultado es verdadero
        assertTrue(resultado);

        // verify para asegurarse de que se llama al método existsById del repository
        verify(tipoUsuarioRepository, times(1)).existsById(1L);
    }

    @Test
    void existeTipo_noExiste_retornaFalse() {
        //esta linea sirve para simular que el repository confirma que no existe un tipo de usuario
        // Mock: repository confirma que no existe el tipo de usuario con ID 999
        when(tipoUsuarioRepository.existsById(999L)).thenReturn(false);

        // esta linea entrega el resultado de la verificación
        boolean resultado = tipoUsuarioService.existeTipo(999L);
        // assertFalse para verificar que el resultado es falso
        assertFalse(resultado);
        // verify para asegurarse de que se llama al método existsById del repository
        verify(tipoUsuarioRepository, times(1)).existsById(999L);
    }
}