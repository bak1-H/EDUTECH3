package com.edutech.sistema.service;

import com.edutech.sistema.model.Curso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;

import java.util.List;


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


    //Este test verifica que el método obtenerCursoPorId
    //retorna un curso cuando se le pasa un ID válido.
    // También verifica que el curso retornado tiene los valores esperados.
    //El test utiliza Mockito para simular el comportamiento del RestTemplate,

    @Test
    void testObtenerCursoPorId_Exitoso() {
        Long cursoId = 1L;
        when(restTemplate.getForObject(anyString(), eq(Curso.class)))
            .thenReturn(cursoEjemplo);
        Curso resultado = cursoService.obtenerCursoPorId(cursoId);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Curso de Java", resultado.getNombreCurso());
        assertEquals("Curso completo de Java", resultado.getDescripcionCurso());
    }


    // Este test verifica que el método obtenerCursoPorId
    //lanza una excepción HttpClientErrorException cuando se intenta obtener un curso
    //con un ID que no existe. Utiliza Mockito para simular el comportamiento del RestTemplate

    @Test
    void testObtenerCursoPorId_NoEncontrado() {
        Long cursoId = 999L;
        when(restTemplate.getForObject(anyString(), eq(Curso.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        assertThrows(HttpClientErrorException.class, () -> {
            cursoService.obtenerCursoPorId(cursoId);
        });
    }

    // Este test verifica que el método obtenerTodosLosCursos
    //retorna una lista de cursos cuando se llama correctamente.
    // También verifica que la lista contiene los cursos esperados.
    // Utiliza Mockito para simular el comportamiento del RestTemplate.
    @Test
    void testObtenerTodosLosCursos_Exitoso() {
        // Arrange
        Curso curso2 = new Curso();
        curso2.setId(2L);
        curso2.setNombreCurso("Curso de Python");
        curso2.setDescripcionCurso("Curso de Python");
        
        Curso[] cursosArray = {cursoEjemplo, curso2};
        when(restTemplate.getForObject(anyString(), eq(Curso[].class)))
            .thenReturn(cursosArray);
        List<Curso> resultado = cursoService.obtenerTodosLosCursos();
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Curso de Java", resultado.get(0).getNombreCurso());
        assertEquals("Curso de Python", resultado.get(1).getDescripcionCurso());
    }


    // Este test verifica que el método obtenerTodosLosCursos
    //retorna una lista vacía cuando no hay cursos disponibles.
    // Utiliza Mockito para simular el comportamiento del RestTemplate.

    @Test
    void testObtenerTodosLosCursos_ListaVacia() {
        when(restTemplate.getForObject(anyString(), eq(Curso[].class)))
            .thenReturn(new Curso[0]);
        List<Curso> resultado = cursoService.obtenerTodosLosCursos();
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
    // Este test verifica que el método obtenerTodosLosCursos
    //lanza una excepción cuando ocurre un error al intentar obtener los cursos.
    // Utiliza Mockito para simular el comportamiento del RestTemplate.
    
    @Test
    void testObtenerTodosLosCursos_Error() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(Curso[].class)))
            .thenThrow(new RuntimeException("Error de conexión"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            cursoService.obtenerTodosLosCursos();
        });
    }
}