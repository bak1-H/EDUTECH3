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
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(cursoEjemplo));
        Optional<Curso> resultado = cursoService.obtenerPorId(1L);

        assertTrue(resultado.isPresent(), "El curso debe existir");
        assertEquals(1L, resultado.get().getId());
        assertEquals("Curso de Java", resultado.get().getNombreCurso());
        assertEquals("Curso completo de Java", resultado.get().getDescripcionCurso());
        
        verify(cursoRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerCursoPorId_NoEncontrado() {
        when(cursoRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<Curso> resultado = cursoService.obtenerPorId(999L);
        assertFalse(resultado.isPresent(), "El curso no debe existir");
        
        verify(cursoRepository, times(1)).findById(999L);
    }

    @Test
    void testObtenerTodosLosCursos() {
        Curso curso2 = new Curso();
        curso2.setId(2L);
        curso2.setNombreCurso("Curso de Python");
        curso2.setDescripcionCurso("Curso completo de Python");
        
        when(cursoRepository.findAll()).thenReturn(Arrays.asList(cursoEjemplo, curso2));
        List<Curso> resultado = cursoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Curso de Java", resultado.get(0).getNombreCurso());
        assertEquals("Curso de Python", resultado.get(1).getNombreCurso());
        
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerTodosLosCursos_ListaVacia() {
        when(cursoRepository.findAll()).thenReturn(Arrays.asList());

        List<Curso> resultado = cursoService.obtenerTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    void testGuardarCurso() {
        when(cursoRepository.save(cursoEjemplo)).thenReturn(cursoEjemplo);

        Curso resultado = cursoService.guardarCurso(cursoEjemplo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Curso de Java", resultado.getNombreCurso());
        assertEquals("Curso completo de Java", resultado.getDescripcionCurso());
        
        verify(cursoRepository, times(1)).save(cursoEjemplo);
    }

    @Test
    void testGuardarCurso_CursoNuevo() {
        // Arrange
        Curso cursoNuevo = new Curso();
        cursoNuevo.setNombreCurso("Curso de JavaScript");
        cursoNuevo.setDescripcionCurso("Curso completo de JavaScript");
        
        Curso cursoGuardado = new Curso();
        cursoGuardado.setId(3L);
        cursoGuardado.setNombreCurso("Curso de JavaScript");
        cursoGuardado.setDescripcionCurso("Curso completo de JavaScript");
        
        when(cursoRepository.save(cursoNuevo)).thenReturn(cursoGuardado);

        // Act
        Curso resultado = cursoService.guardarCurso(cursoNuevo);

        // Assert
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals("Curso de JavaScript", resultado.getNombreCurso());
        
        verify(cursoRepository, times(1)).save(cursoNuevo);
    }

    @Test
    void testEliminarCurso() {
        // Arrange
        doNothing().when(cursoRepository).deleteById(1L);

        // Act
        cursoService.eliminarCurso(1L);

        // Assert
        verify(cursoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testExisteCurso_Existe() {
        // Arrange
        when(cursoRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean resultado = cursoRepository.existsById(1L);

        // Assert
        assertTrue(resultado);
        verify(cursoRepository, times(1)).existsById(1L);
    }

    @Test
    void testExisteCurso_NoExiste() {
        // Arrange
        when(cursoRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean resultado = cursoRepository.existsById(999L);

        // Assert
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