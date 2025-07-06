package com.edutech.usuario.controller;

import com.edutech.usuario.model.Usuario;
import com.edutech.usuario.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/usuarios")
@Tag(name = "Usuarios con ATHEOAS", description = "Operacion GET con HATEOAS")
public class UsuarioControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioControllerV2.class);

    @Autowired
    private UsuarioService usuarioService;

    // READ - Obtener todos los usuarios
    @Operation(summary = "Obtener todos los usuarios")
    @Description("Este endpoint permite obtener una lista de todos los usuarios registrados en el sistema.")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> obtenerTodos() {
        try {
            List<Usuario> usuarios = usuarioService.obtenerTodos();
            
            List<EntityModel<Usuario>> usuariosModel = usuarios.stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

            CollectionModel<EntityModel<Usuario>> collection = CollectionModel.of(usuariosModel);
            collection.add(linkTo(methodOn(UsuarioControllerV2.class).obtenerTodos()).withSelfRel());
            
            return ResponseEntity.ok(collection);
        } catch (Exception e) {
            logger.error("Error al obtener usuarios: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // READ - Obtener usuario por RUT
    @Operation(summary = "Obtener usuario por RUT")
    @Description("Este endpoint permite obtener un usuario específico utilizando su RUT.")
    @GetMapping("/{rut}")
    public ResponseEntity<EntityModel<Usuario>> obtenerPorRut(@PathVariable String rut) {
        try {
            Usuario usuario = usuarioService.obtenerPorRut(rut);
            if (usuario != null) {
                EntityModel<Usuario> usuarioModel = agregarLinks(usuario);
                return ResponseEntity.ok(usuarioModel);
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            logger.error("Error de validación al obtener usuario {}: {}", rut, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error interno al obtener usuario {}: {}", rut, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Método privado para agregar enlaces HATEOAS
    private EntityModel<Usuario> agregarLinks(Usuario usuario) {
        String rutString = usuario.getRut().toString();
        
        EntityModel<Usuario> usuarioModel = EntityModel.of(usuario);
        
        usuarioModel.add(linkTo(methodOn(UsuarioControllerV2.class).obtenerPorRut(rutString)).withSelfRel());
        usuarioModel.add(linkTo(methodOn(UsuarioControllerV2.class).obtenerTodos()).withRel("usuarios"));
        return usuarioModel;
    }
}