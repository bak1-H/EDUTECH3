package com.edutech.pago.controller;

import com.edutech.pago.model.Pago;
import com.edutech.pago.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/pagos")
@Tag(name = "Pagos HATEOAS", description = "Operaciones GET con HATEOAS")
public class PagoControllerV2 {

    @Autowired
    private PagoService pagoService;

    //

    @Operation(summary = "Obtener todos los pagos", description = "Obtiene una lista de todos los pagos con enlaces HATEOAS")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> obtenerTodos() {
        List<Pago> pagos = pagoService.obtenerTodos();
        List<EntityModel<Pago>> pagosModel = pagos.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Pago>> collection = CollectionModel.of(pagosModel);
        collection.add(linkTo(methodOn(PagoControllerV2.class).obtenerTodos()).withSelfRel());
        return ResponseEntity.ok(collection);
    }

    @Operation(summary = "Obtener pago por ID", 
    description = "Obtiene un pago específico por su ID con enlaces HATEOAS")

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pago>> obtenerPorId(@PathVariable Long id) {
        Optional<Pago> pagoOpt = pagoService.obtenerPorId(id);
        return pagoOpt
                .map(pago -> ResponseEntity.ok(toModel(pago)))
                .orElse(ResponseEntity.notFound().build());
    }

    // GET BY USUARIO RUT
    @Operation(summary = "Obtener pagos por RUT de usuario", 
    description = "Obtiene una lista de pagos asociados a un usuario por su RUT con enlaces HATEOAS")

    @GetMapping("/usuario/{usuarioRut}")
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> obtenerPorUsuarioRut(@PathVariable Long usuarioRut) {
        List<Pago> pagos = pagoService.obtenerPorUsuarioRut(usuarioRut);
        List<EntityModel<Pago>> pagosModel = pagos.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Pago>> collection = CollectionModel.of(pagosModel);
        collection.add(linkTo(methodOn(PagoControllerV2.class).obtenerPorUsuarioRut(usuarioRut)).withSelfRel());
        collection.add(linkTo(methodOn(PagoControllerV2.class).obtenerTodos()).withRel("todos"));
        return ResponseEntity.ok(collection);
    }

    // GET BY CURSO ID
    @Operation(summary = "Obtener pagos por ID de curso", 
    description = "Obtiene una lista de pagos asociados a un curso por su ID con enlaces HATEOAS")
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> obtenerPorCursoId(@PathVariable Long cursoId) {
        List<Pago> pagos = pagoService.obtenerPorCursoId(cursoId);
        List<EntityModel<Pago>> pagosModel = pagos.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Pago>> collection = CollectionModel.of(pagosModel);
        collection.add(linkTo(methodOn(PagoControllerV2.class).obtenerPorCursoId(cursoId)).withSelfRel());
        collection.add(linkTo(methodOn(PagoControllerV2.class).obtenerTodos()).withRel("todos"));
        return ResponseEntity.ok(collection);
    }


    // Método privado para agregar links HATEOAS a cada Pago
    private EntityModel<Pago> toModel(Pago pago) {
        EntityModel<Pago> model = EntityModel.of(pago);
        model.add(linkTo(methodOn(PagoControllerV2.class).obtenerPorId(pago.getId())).withSelfRel());
        model.add(linkTo(methodOn(PagoControllerV2.class).obtenerTodos()).withRel("todos"));
        model.add(linkTo(methodOn(PagoControllerV2.class).obtenerPorUsuarioRut(pago.getUsuarioRut())).withRel("pagos-usuario"));
        model.add(linkTo(methodOn(PagoControllerV2.class).obtenerPorCursoId(pago.getCursoId())).withRel("pagos-curso"));
        return model;
    }
}