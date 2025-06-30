package com.edutech.curso.controller;
import com.edutech.curso.model.Curso;
import com.edutech.curso.service.CursoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/curso") 
@Tag(name = "Cursos", description = "Operaciones CRUD para gestionar cursos del sistema")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Operation(summary = "Actualizar curso por ID", description = "Actualiza un curso existente basado en su ID.")
    @Description("Este endpoint permite actualizar un curso existente. Si el curso no existe, se retornará un error 404.")
    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizarCurso(@PathVariable Long id, @RequestBody Curso curso) {
        Optional<Curso> cursoExistente = cursoService.obtenerPorId(id);
        
        if (cursoExistente.isPresent()) {
            curso.setId(id);
            Curso cursoActualizado = cursoService.guardarCurso(curso);
            return ResponseEntity.ok(cursoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener curso por ID", description = "Obtiene un curso específico basado en su ID.")
    @Description("Este endpoint permite obtener un curso específico. Si el curso no existe, se retornará un error 404.")
    public ResponseEntity<Curso> obtenerCursoPorId(@PathVariable Long id) {
        Optional<Curso> curso = cursoService.obtenerPorId(id);
        return curso.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nuevo curso", description = "Crea un nuevo curso en el sistema.")
    @Description("Este endpoint permite crear un nuevo curso. Si el curso ya existe, se retornará un error 409.")
    @PostMapping
    public ResponseEntity<Curso> crearCurso(@RequestBody Curso curso) {
        Curso cursoCreado = cursoService.guardarCurso(curso);
        return ResponseEntity.ok(cursoCreado);
    }

    @Operation(summary = "Obtener todos los cursos", description = "Obtiene una lista de todos los cursos registrados.")
    @Description("Este endpoint permite obtener una lista de todos los cursos registrados en el sistema.")
    @GetMapping
    public ResponseEntity<List<Curso>> obtenerTodosLosCursos() {
        List<Curso> cursos = cursoService.obtenerTodos();
        return ResponseEntity.ok(cursos);
    }

    @Operation(summary = "Eliminar curso por ID", description = "Elimina un curso específico basado en su ID.")
    @Description("Este endpoint permite eliminar un curso existente. Si el curso no existe, se retornará un error 404.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        cursoService.eliminarCurso(id);
        return ResponseEntity.noContent().build();
    }
}