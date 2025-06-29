package com.edutech.curso.service;

import com.edutech.curso.model.Curso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private RestTemplate restTemplate;

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



    //Test para el servicio CursoService
    //Obtiene un curso por ID.
    @Test
    void testObtenerCursoPorId_Exitoso() {
        when(restTemplate.getForObject(anyString(), eq(Curso.class))).thenReturn(cursoEjemplo);

        Optional<Curso> resultado = cursoService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(restTemplate).getForObject(anyString(), eq(Curso.class));
    }

    //Test para obtener un curso por ID que no existe.
    //Retorna un Optional vacío.
    @Test
    void testObtenerCursoPorId_NoEncontrado() {
        when(restTemplate.getForObject(anyString(), eq(Curso.class))).thenReturn(null);

        Optional<Curso> resultado = cursoService.obtenerPorId(999L);

        assertFalse(resultado.isPresent());
    }

    //Test para obtener todos los cursos.
    //Retorna una lista con un curso.
    @Test
    void testObtenerTodosLosCursos() {
        Curso[] cursosArray = {cursoEjemplo};
        when(restTemplate.getForObject(anyString(), eq(Curso[].class))).thenReturn(cursosArray);

        List<Curso> resultado = cursoService.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals("Curso de Java", resultado.get(0).getNombreCurso());
    }

    //Test para guardar un curso.
    //Retorna el curso guardado.
    @Test
    void testGuardarCurso() {
        when(restTemplate.postForObject(anyString(), any(Curso.class), eq(Curso.class)))
            .thenReturn(cursoEjemplo);

        Curso resultado = cursoService.guardarCurso(cursoEjemplo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }
    
    //Test eliminar un curso.
    //Verifica que se llame al método delete de un curso
    @Test
    void testEliminarCurso() {
        doNothing().when(restTemplate).delete(anyString());

        cursoService.eliminarCurso(1L);

        verify(restTemplate).delete(anyString());
    }
}