package com.edutech.curso.service;

import com.edutech.curso.model.Curso;
import com.edutech.curso.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository; // ← Cambio principal: Repository, no RestTemplate

    @InjectMocks
    private CursoService cursoService;

    private Curso cursoEjemplo;

    @BeforeEach
    void setUp() {
        cursoEjemplo = new Curso();
        cursoEjemplo.setId(1L);
        cursoEjemplo.setNombreCurso("Curso de Java");
        cursoEjemplo.setDescripcionCurso("Curso completo de Java");
    }

    // === TESTS PARA OPERACIONES CRUD LOCALES ===

    @Test
    void testObtenerCursoPorId_Exitoso() {
        // Simular el comportamiento del repositorio
        // cuando se busca un curso por ID
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(cursoEjemplo));
        // Llamar al método del servicio
        // que obtiene el curso por ID
        Optional<Curso> resultado = cursoService.obtenerPorId(1L);
        // Verificar que el resultado no es nulo
        // y que contiene el curso esperado
        assertTrue(resultado.isPresent(), "El curso debe existir");
        assertEquals(1L, resultado.get().getId());
        assertEquals("Curso de Java", resultado.get().getNombreCurso());
        assertEquals("Curso completo de Java", resultado.get().getDescripcionCurso());
        // Verificar que el repositorio fue llamado una vez
        // para buscar el curso por ID
        verify(cursoRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerCursoPorId_NoEncontrado() {
        // Simular el comportamiento del repositorio
        // cuando se busca un curso por un ID que no existe
        when(cursoRepository.findById(999L)).thenReturn(Optional.empty());
        // Llamar al método del servicio
        // que obtiene el curso por ID
        Optional<Curso> resultado = cursoService.obtenerPorId(999L);

        // Verificar que el resultado es un Optional vacío
        // porque el curso no existe
        assertFalse(resultado.isPresent(), "El curso no debe existir");
        // Verificar que el repositorio fue llamado una vez
        // para buscar el curso por ID
        verify(cursoRepository, times(1)).findById(999L);
    }

    @Test
    void testObtenerTodosLosCursos() {

        // Simular el comportamiento del repositorio
        // cuando se obtienen todos los cursos
        Curso curso2 = new Curso();
        curso2.setId(2L);
        curso2.setNombreCurso("Curso de Python");
        curso2.setDescripcionCurso("Curso completo de Python");
        
        // Simular que el repositorio devuelve una lista con dos cursos
        // y verificar que el servicio devuelve la lista esperada
        when(cursoRepository.findAll()).thenReturn(Arrays.asList(cursoEjemplo, curso2));

        // Llamar al método del servicio
        // que obtiene todos los cursos
        List<Curso> resultado = cursoService.obtenerTodos();


        // Verificar que el resultado no es nulo
        assertNotNull(resultado);
        // Verificar que la lista contiene dos cursos
        assertEquals(2, resultado.size());
        // Verificar que los cursos tienen los nombres esperados
        assertEquals("Curso de Java", resultado.get(0).getNombreCurso());
        // Verificar que el segundo curso tiene el nombre esperado
        assertEquals("Curso de Python", resultado.get(1).getNombreCurso());
        // Verificar que el repositorio fue llamado una vez
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerTodosLosCursos_ListaVacia() {
        // Simular el comportamiento del repositorio
        when(cursoRepository.findAll()).thenReturn(Arrays.asList());
        // Llamar al método del servicio
        List<Curso> resultado = cursoService.obtenerTodos();
        // Verificar que el resultado no es nulo
        assertNotNull(resultado);
        // Verificar que la lista está vacía
        assertTrue(resultado.isEmpty());
        // Verificar que el repositorio fue llamado una vez
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    void testGuardarCurso() {
        //simular el comportamiento del repositorio
        // cuando se guarda un curso existente
        when(cursoRepository.save(cursoEjemplo)).thenReturn(cursoEjemplo);
        // llama al método del servicio
        Curso resultado = cursoService.guardarCurso(cursoEjemplo);

        // Verifica que el resultado no es nulo
        assertNotNull(resultado);
        // Verifica que el curso guardado tiene los valores esperados
        assertEquals(1L, resultado.getId());
        // Verifica que el nombre y la descripción del curso son correctos
        assertEquals("Curso de Java", resultado.getNombreCurso());
        assertEquals("Curso completo de Java", resultado.getDescripcionCurso());
        // Verifica que el repositorio fue llamado una vez
        verify(cursoRepository, times(1)).save(cursoEjemplo);
    }

    @Test
    void testGuardarCurso_CursoNuevo() {
        //aca se simula el comportamiento del repositorio
        // cuando se guarda un curso nuevo
        Curso cursoNuevo = new Curso();
        cursoNuevo.setNombreCurso("Curso de JavaScript");
        cursoNuevo.setDescripcionCurso("Curso completo de JavaScript");
        
        // Simular que el repositorio guarda el curso nuevo
        // y devuelve el curso guardado con un ID asignado
        Curso cursoGuardado = new Curso();
        cursoGuardado.setId(3L);
        cursoGuardado.setNombreCurso("Curso de JavaScript");
        cursoGuardado.setDescripcionCurso("Curso completo de JavaScript");
        
        // Simular que el repositorio guarda el curso nuevo y devuelve el curso guardado
        // con un ID asignado
        when(cursoRepository.save(cursoNuevo)).thenReturn(cursoGuardado);

        // Llamar al método del servicio
        // que guarda el curso nuevo
        Curso resultado = cursoService.guardarCurso(cursoNuevo);

        // Verificar que el resultado no es nulo
        assertNotNull(resultado);
        // Verificar que el curso guardado tiene los valores esperados
        assertEquals(3L, resultado.getId());
        // Verificar que el nombre y la descripción del curso son correctos
        assertEquals("Curso de JavaScript", resultado.getNombreCurso());
        
        assertEquals("Curso completo de JavaScript", resultado.getDescripcionCurso());
        verify(cursoRepository, times(1)).save(cursoNuevo);
    }

    @Test
    void testEliminarCurso() {

        //Esta liena simula el comportamiento del repositorio
        // cuando se elimina un curso por ID
        doNothing().when(cursoRepository).deleteById(1L);
        // esta linea llama al método del servicio
        // que elimina el curso por ID
        cursoService.eliminarCurso(1L);

        // esta linea verifica que el repositorio fue llamado una vez
        // para eliminar el curso por ID
        verify(cursoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testExisteCurso_Existe() {
        // esta linea simula el comportamiento del repositorio
        // cuando se verifica si un curso existe por ID
        when(cursoRepository.existsById(1L)).thenReturn(true);

        //esta linea llama al método del servicio
        // que verifica si el curso existe por ID
        boolean resultado = cursoRepository.existsById(1L);

        // esta linea verifica que el resultado es verdadero
        assertTrue(resultado);
        // esta linea verifica que el repositorio fue llamado una vez
        verify(cursoRepository, times(1)).existsById(1L);
    }

    @Test
    void testExisteCurso_NoExiste() {
        //esta line simula el comportamiento del repositorio
        when(cursoRepository.existsById(999L)).thenReturn(false);
        boolean resultado = cursoRepository.existsById(999L);
        assertFalse(resultado);
        verify(cursoRepository, times(1)).existsById(999L);
    }

    @Test
    void testActualizarCurso() {
        Curso cursoActualizado = new Curso();
        cursoActualizado.setId(1L);
        cursoActualizado.setNombreCurso("Curso de Java Avanzado");
        cursoActualizado.setDescripcionCurso("Curso avanzado de Java");
        
        when(cursoRepository.save(cursoActualizado)).thenReturn(cursoActualizado);
        cursoService.actualizarCurso(cursoActualizado);
        verify(cursoRepository, times(1)).save(cursoActualizado);
    }
}