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
        //esta linea sirve para simular que el microservicio devuelve un curso
        // Mock del RestTemplate para devolver un curso específico
        Long cursoId = 1L;
        when(restTemplate.getForEntity(eq("http://localhost:8084/api/curso/" + cursoId), eq(Curso.class)))
            .thenReturn(responseEntity);
        
        //esta linea entrega el resultado de la obtención
        Curso resultado = cursoService.obtenerCursoPorId(cursoId);
        
        // assertNotNull para verificar que el resultado no es nulo
        assertNotNull(resultado);
        // assertEquals para verificar que el ID, nombre y descripción del curso son correctos
        // y que coinciden con los valores esperados
        assertEquals(1L, resultado.getId());
        // assertEquals para verificar que el nombre del curso es "Java Básico"
        // y que la descripción es "Curso de Java para principiantes"
        assertEquals("Java Básico", resultado.getNombreCurso());
        // assertEquals para verificar que la descripción del curso es "Curso de Java para principiantes"
        // y que coincide con el valor esperado
        assertEquals("Curso de Java para principiantes", resultado.getDescripcionCurso());
        
        // verify para asegurarse de que se llama al método getForEntity del RestTemplate
        // y que se ha ejecutado una vez con los parámetros correctos
        verify(restTemplate, times(1)).getForEntity(
            eq("http://localhost:8084/api/curso/" + cursoId), 
            eq(Curso.class)
        );
    }

    @Test
    void obtenerCursoPorId_NoEncontrado() {
        // esta linea sirve para simular que el microservicio devuelve un error 404
        // Mock del RestTemplate para lanzar una excepción cuando no se encuentra el curso
        Long cursoId = 999L;
        // cuando el RestTemplate intenta obtener un curso con un ID que no existe
        // se lanza una excepción HttpClientErrorException con el estado NOT_FOUND
        when(restTemplate.getForEntity(eq("http://localhost:8084/api/curso/" + cursoId), eq(Curso.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        
        //esta lnea entrega el resultado de la obtención
        // Actuar y verificar que el resultado es nulo
        Curso resultado = cursoService.obtenerCursoPorId(cursoId);
        
        // assertNull para verificar que el resultado es nulo
        // y que no se ha encontrado el curso con el ID especificado
        assertNull(resultado);
        // verify para asegurarse de que se llama al método getForEntity del RestTemplate
        // y que se ha ejecutado una vez con los parámetros correctos
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Curso.class));
    }

    @Test
    void obtenerCursoPorId_ErrorConexion() {
        // esta linea sirve para simular que el microservicio no está disponible
        // Mock del RestTemplate para lanzar una excepción ResourceAccessException
        Long cursoId = 1L;
        when(restTemplate.getForEntity(eq("http://localhost:8084/api/curso/" + cursoId), eq(Curso.class)))
            .thenThrow(new ResourceAccessException("Microservicio no disponible"));
        
        //esta linea entrega el resultado de la obtención
        // Actuar y verificar que el resultado es nulo
        Curso resultado = cursoService.obtenerCursoPorId(cursoId);
        
        // assertNotNull para verificar que el resultado no es nulo
        assertNull(resultado);
        // verify para asegurarse de que se llama al método getForEntity del RestTemplate
        // y que se ha ejecutado una vez con los parámetros correctos
        // aunque no se haya podido obtener el curso debido a un error de conexión
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Curso.class));
    }

    @Test
    void obtenerTodosLosCursos_Exitoso() {
        //esta linea sirve para simular que el microservicio devuelve una lista de cursos
        // Mock del RestTemplate para devolver una lista de cursos
        Curso curso2 = new Curso();
        curso2.setId(2L);
        curso2.setNombreCurso("Python Básico");
        curso2.setDescripcionCurso("Curso de Python para principiantes");
        
        // Crear un array de cursos y una respuesta HTTP con estado OK
        // que contiene los cursos simulados    
        Curso[] cursosArray = {curso, curso2};
        responseEntityArray = new ResponseEntity<>(cursosArray, HttpStatus.OK);
        
        // cuando el RestTemplate intenta obtener todos los cursos
        // se devuelve la respuesta con los cursos simulados
        when(restTemplate.getForEntity(eq("http://localhost:8084/api/curso"), eq(Curso[].class)))
            .thenReturn(responseEntityArray);
        
        //esta linea entrega el resultado de la obtención
        // Actuar y verificar que se obtienen todos los cursos
        List<Curso> resultado = cursoService.obtenerTodosLosCursos();
        
        // assertNotNull para verificar que el resultado no es nulo
        assertNotNull(resultado);
        // assertFalse para verificar que la lista de cursos no está vacía
        assertEquals(2, resultado.size());
        // assertEquals para verificar que los cursos obtenidos son los esperados
        assertEquals("Java Básico", resultado.get(0).getNombreCurso());
        assertEquals("Python Básico", resultado.get(1).getNombreCurso());
        
        // verify para asegurarse de que se llama al método getForEntity del RestTemplate
        // y que se ha ejecutado una vez con la URL correcta y el tipo de respuesta
        verify(restTemplate, times(1)).getForEntity(
            eq("http://localhost:8084/api/curso"), 
            eq(Curso[].class)
        );
    }

    @Test
    void obtenerTodosLosCursos_ListaVacia() {
        //esta linea sirve para simular que el microservicio devuelve una lista vacía
        // Mock del RestTemplate para devolver una lista vacía de cursos
        Curso[] cursosVacios = new Curso[0];
        responseEntityArray = new ResponseEntity<>(cursosVacios, HttpStatus.OK);
        // cuando el RestTemplate intenta obtener todos los cursos
        // se devuelve una respuesta con una lista vacía de cursos
        when(restTemplate.getForEntity(eq("http://localhost:8084/api/curso"), eq(Curso[].class)))
            .thenReturn(responseEntityArray);
        
        //esta linea entrega el resultado de la obtención
        // Actuar y verificar que se obtiene una lista vacía
        List<Curso> resultado = cursoService.obtenerTodosLosCursos();
        
        // assertNotNull para verificar que el resultado no es nulo
        assertNotNull(resultado);
        // assertTrue para verificar que la lista de cursos está vacía
        assertTrue(resultado.isEmpty());
        // verify para asegurarse de que se llama al método getForEntity del RestTemplate
        // y que se ha ejecutado una vez con la URL correcta y el tipo de respuesta
        // aunque no se hayan encontrado cursos
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Curso[].class));
    }

    @Test
    void obtenerTodosLosCursos_ErrorConexion() {
        //esta linea sirve para simular que el microservicio no está disponible
        // Mock del RestTemplate para lanzar una excepción ResourceAccessException
        when(restTemplate.getForEntity(eq("http://localhost:8084/api/curso"), eq(Curso[].class)))
            .thenThrow(new ResourceAccessException("Microservicio no disponible"));

        // esta linea entrega el resultado de la obtención
        // Actuar y verificar que se obtiene una lista vacía
        List<Curso> resultado = cursoService.obtenerTodosLosCursos();
        
        // esta linea verifica que el resultado no es nulo
        assertNotNull(resultado);
        // assertTrue para verificar que la lista de cursos está vacía
        assertTrue(resultado.isEmpty());
        // verify para asegurarse de que se llama al método getForEntity del RestTemplate
        // y que se ha ejecutado una vez con la URL correcta y el tipo de respuesta
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(Curso[].class));
    }

    @Test
    void obtenerTodosLosCursos_RespuestaNull() {
        //esta linea sirve para simular que el microservicio devuelve una respuesta nula
        // Mock del RestTemplate para devolver una respuesta nula
        ResponseEntity<Curso[]> responseNull = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.getForEntity(eq("http://localhost:8084/api/curso"), eq(Curso[].class)))
            .thenReturn(responseNull);
        
        //esta linea entrega el resultado de la obtención
        List<Curso> resultado = cursoService.obtenerTodosLosCursos();
        
        //assertNotNull para verificar que el resultado no es nulo
        assertNotNull(resultado);
        // assertTrue para verificar que la lista de cursos está vacía
        // ya que la respuesta del microservicio fue nula
        assertTrue(resultado.isEmpty());
    }
}