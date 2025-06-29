package com.edutech.curso.controller;

import com.edutech.curso.model.Curso;
import com.edutech.curso.service.CursoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(CursoController.class)

public class cursoControllerTest {

@Autowired
private MockMvc mockMvc;

@MockitoBean
private CursoService cursoService;

@Autowired
private ObjectMapper objectMapper; // ObjectMapper se usa para convertir objetos a JSON y viceversa

private Curso curso1;
private Curso curso2;

@BeforeEach
void setUp() {
    curso1 = new Curso();
    curso1.setId(1L);
    curso1.setNombreCurso("Curso de Java");
    curso1.setDescripcionCurso("Aprende Java desde cero");

    curso2 = new Curso();
    curso2.setId(2L);
    curso2.setNombreCurso("Curso de Python");
    curso2.setDescripcionCurso("Aprende Python desde cero");
}


// Test para Guardar un Curso
// Este test verifica que al crear un curso, el servicio es llamado correctamente
@Test
void testCrearCurso() throws Exception {
    when(cursoService.guardarCurso(any(Curso.class))).thenReturn(curso1);

    mockMvc.perform(post("/api/curso")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(curso1)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombreCurso").value("Curso de Java"))
            .andExpect(jsonPath("$.descripcionCurso").value("Aprende Java desde cero"));
    verify(cursoService, times(1)).guardarCurso(any(Curso.class));
}


// Test para Obtener un Cursos
// Este test verifica que al solicitar un get, se retorne los cursos
@Test
void testObtenerCursoAgregados() throws Exception {
    List<Curso> cursos = new ArrayList<>(Arrays.asList(curso1, curso2));
    when(cursoService.obtenerTodos()).thenReturn(cursos);

    mockMvc.perform(get("/api/curso"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].nombreCurso").value("Curso de Java"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].nombreCurso").value("Curso de Python"));
    verify(cursoService, times(1)).obtenerTodos();
}

//Test para obtener un Curso por ID
// Este test verifica que al solicitar un get con un ID específico, se retorne el curso

@Test
void testObtenerCursoPorId() throws Exception {
    when(cursoService.obtenerPorId(1L)).thenReturn(Optional.of(curso1));

    mockMvc.perform(get("/api/curso/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombreCurso").value("Curso de Java"))
            .andExpect(jsonPath("$.descripcionCurso").value("Aprende Java desde cero"));
    verify(cursoService, times(1)).obtenerPorId(1L);

}


//Test para actualizar un Curso
//se crea un curso actualizado y se verifica que el servicio es llamado correctamente
// Este test verifica que al actualizar un curso, el servicio es llamado correctamente
@Test
void testActualizarCurso() throws Exception {
    Curso cursoActualizado = new Curso();
    cursoActualizado.setId(1L);
    cursoActualizado.setNombreCurso("Curso de Java Avanzado");
    cursoActualizado.setDescripcionCurso("Aprende Java Avanzado");

    when(cursoService.obtenerPorId(1L)).thenReturn(Optional.of(curso1));
    when(cursoService.guardarCurso(any(Curso.class))).thenReturn(cursoActualizado);

    mockMvc.perform(put("/api/curso/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cursoActualizado)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombreCurso").value("Curso de Java Avanzado"))
            .andExpect(jsonPath("$.descripcionCurso").value("Aprende Java Avanzado"));

    verify(cursoService, times(1)).guardarCurso(any(Curso.class));
}

// Test para eliminar un Curso
// Este test verifica que al eliminar un curso, el servicio es llamado correctamente

@Test
void testEliminarCurso() throws Exception {
    when(cursoService.obtenerPorId(1L)).thenReturn(Optional.of(curso1));

    mockMvc.perform(delete("/api/curso/{id}", 1L))
            .andExpect(status().isNoContent());

    verify(cursoService, times(1)).eliminarCurso(1L);
}
}