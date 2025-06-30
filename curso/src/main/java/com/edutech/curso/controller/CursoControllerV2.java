package com.edutech.curso.controller;

import com.edutech.curso.model.Curso;
import com.edutech.curso.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/curso")
@Tag(name = "Cursos con HATEOAS", description = "Operaciones GET con HATEOAS")
public class CursoControllerV2 {

    @Autowired
    private CursoService cursoService;

    // GET ALL (HATEOAS)
    @Operation(summary = "Obtener todos los cursos", description = "Obtiene una lista de todos los cursos registrados.")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Curso>>> obtenerTodosLosCursos() {
        List<Curso> cursos = cursoService.obtenerTodos();
        List<EntityModel<Curso>> cursosModel = cursos.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Curso>> collection = CollectionModel.of(cursosModel);
        collection.add(linkTo(methodOn(CursoControllerV2.class).obtenerTodosLosCursos()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    // GET BY ID (HATEOAS)
    @Operation(summary = "Obtener curso por ID", description = "Obtiene un curso específico basado en su ID.")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Curso>> obtenerCursoPorId(@PathVariable Long id) {
        Optional<Curso> cursoOpt = cursoService.obtenerPorId(id);
        return cursoOpt
                .map(curso -> ResponseEntity.ok(toModel(curso)))
                .orElse(ResponseEntity.notFound().build());
    }


    // Método privado para agregar links HATEOAS a cada Curso
    private EntityModel<Curso> toModel(Curso curso) {
        EntityModel<Curso> model = EntityModel.of(curso);
        model.add(linkTo(methodOn(CursoControllerV2.class).obtenerCursoPorId(curso.getId())).withSelfRel());
        model.add(linkTo(methodOn(CursoControllerV2.class).obtenerTodosLosCursos()).withRel("cursos"));
        return model;
    }
}