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
import org.springframework.web.client.ResourceAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private RestTemplate restTemplate; // Corregido: Mock del RestTemplate, no Repository

    @InjectMocks
    private CursoService cursoService;

    private Curso curso;
    private ResponseEntity<Curso> responseEntity;
    private ResponseEntity<Curso[]> responseEntityArray;

    @BeforeEach
    void setUp() {
        curso = new Curso();
        curso.setId(1L);
        curso.setNombreCurso("Java Básico");
        curso.setDescripcionCurso("Curso de Java para principiantes");

        responseEntity = new ResponseEntity<>(curso, HttpStatus.OK);
    }

    @Test
    void obtenerCursoPorId_Exitoso() {
        // Arrange
        Long cursoId = 1L;
        when(restTemplate.getForEntity(eq("http://localhost:8084/api/curso/" + cursoId), eq(Curso.class)))
            .thenReturn(responseEntity);
        
        // Act
        Curso resultado = cursoService.obtenerCursoPorId(cursoId);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Java Básico", resultado.getNombreCurso());
        assertEquals("Curso de Java para principiantes", resultado.getDescripcionCurso());
        
        verify(restTemplate, times(1)).getForEntity(
            eq("http://localhost:8084/api/curso/" + cursoId), 
            eq(Curso.class)
        );
    }

    @Test
    void obtenerCursoPorId_NoEncontrado() {
        // Arrange
        Long cursoId = 999L;
        when(restTemplate.getForEntity(eq("http://localhost:8084/api/curso/" + cursoId), eq(Curso.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        
        // Act
        Curso resultado = cursoService.obtenerCursoPorId(cursoId);
        
        // Assert
        assertNull(resultado);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Curso.class));
    }

    @Test
    void obtenerCursoPorId_ErrorConexion() {
        // Arrange
        Long cursoId = 1L;
        when(restTemplate.getForEntity(eq("http://localhost:8084/api/curso/" + cursoId), eq(Curso.class)))
            .thenThrow(new ResourceAccessException("Microservicio no disponible"));
        
        // Act
        Curso resultado = cursoService.obtenerCursoPorId(cursoId);
        
        // Assert
        assertNull(resultado);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Curso.class));
    }

    @Test
    void obtenerTodosLosCursos_Exitoso() {
        // Arrange
        Curso curso2 = new Curso();
        curso2.setId(2L);
        curso2.setNombreCurso("Python Básico");
        curso2.setDescripcionCurso("Curso de Python para principiantes");
        
        Curso[] cursosArray = {curso, curso2};
        responseEntityArray = new ResponseEntity<>(cursosArray, HttpStatus.OK);
        
        when(restTemplate.getForEntity(eq("http://localhost:8084/api/curso"), eq(Curso[].class)))
            .thenReturn(responseEntityArray);
        
        // Act
        List<Curso> resultado = cursoService.obtenerTodosLosCursos();
        
        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Java Básico", resultado.get(0).getNombreCurso());
        assertEquals("Python Básico", resultado.get(1).getNombreCurso());
        
        verify(restTemplate, times(1)).getForEntity(
            eq("http://localhost:8084/api/curso"), 
            eq(Curso[].class)
        );
    }

    @Test
    void obtenerTodosLosCursos_ListaVacia() {
        // Arrange
        Curso[] cursosVacios = new Curso[0];
        responseEntityArray = new ResponseEntity<>(cursosVacios, HttpStatus.OK);
        
        when(restTemplate.getForEntity(eq("http://localhost:8084/api/curso"), eq(Curso[].class)))
            .thenReturn(responseEntityArray);
        
        // Act
        List<Curso> resultado = cursoService.obtenerTodosLosCursos();
        
        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Curso[].class));
    }

    @Test
    void obtenerTodosLosCursos_ErrorConexion() {
        // Arrange
        when(restTemplate.getForEntity(eq("http://localhost:8084/api/curso"), eq(Curso[].class)))
            .thenThrow(new ResourceAccessException("Microservicio no disponible"));

        // Act
        List<Curso> resultado = cursoService.obtenerTodosLosCursos();
        
        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Curso[].class));
    }

    @Test
    void obtenerTodosLosCursos_RespuestaNull() {
        // Arrange
        ResponseEntity<Curso[]> responseNull = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.getForEntity(eq("http://localhost:8084/api/curso"), eq(Curso[].class)))
            .thenReturn(responseNull);
        
        // Act
        List<Curso> resultado = cursoService.obtenerTodosLosCursos();
        
        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}