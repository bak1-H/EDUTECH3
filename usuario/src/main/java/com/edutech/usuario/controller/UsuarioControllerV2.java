package com.edutech.usuario.controller;

import com.edutech.usuario.model.Usuario;
import com.edutech.usuario.model.UsuarioRequest;
import com.edutech.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
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
@CrossOrigin(origins = "*")
public class UsuarioControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioControllerV2.class);

    @Autowired
    private UsuarioService usuarioService;

    // CREATE - Crear usuario
    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> crear(@RequestBody UsuarioRequest request) {
        try {
            // Verificar si el RUT ya existe
            Usuario usuarioExistente = usuarioService.obtenerPorRut(request.getRut().toString());
            if (usuarioExistente != null) {
                logger.warn("Intento de crear usuario con RUT duplicado: {}", request.getRut());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            
            Usuario usuario = usuarioService.crearUsuario(request);
            EntityModel<Usuario> usuarioModel = agregarLinks(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioModel);
        } catch (RuntimeException e) {
            logger.error("Error de validación al crear usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Error interno al crear usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // READ - Obtener todos los usuarios
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

    // UPDATE - Actualizar usuario
    @PutMapping("/{rut}")
    public ResponseEntity<EntityModel<Usuario>> actualizar(@PathVariable String rut, 
                                                          @RequestBody UsuarioRequest request) {
        try {
            Usuario usuario = usuarioService.actualizarUsuario(rut, request);
            if (usuario != null) {
                EntityModel<Usuario> usuarioModel = agregarLinks(usuario);
                return ResponseEntity.ok(usuarioModel);
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            logger.error("Error de validación al actualizar usuario {}: {}", rut, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Error interno al actualizar usuario {}: {}", rut, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE - Eliminar usuario
    @DeleteMapping("/{rut}")
    public ResponseEntity<Void> eliminar(@PathVariable String rut) {
        try {
            boolean eliminado = usuarioService.eliminarUsuario(rut);
            if (eliminado) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            logger.error("Error de validación al eliminar usuario {}: {}", rut, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Error interno al eliminar usuario {}: {}", rut, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Método privado para agregar enlaces HATEOAS
    private EntityModel<Usuario> agregarLinks(Usuario usuario) {
        String rutString = usuario.getRut().toString();
        
        EntityModel<Usuario> usuarioModel = EntityModel.of(usuario);
        
        usuarioModel.add(linkTo(methodOn(UsuarioControllerV2.class).obtenerPorRut(rutString)).withSelfRel());
        usuarioModel.add(linkTo(methodOn(UsuarioControllerV2.class).actualizar(rutString, null)).withRel("actualizar"));
        usuarioModel.add(linkTo(methodOn(UsuarioControllerV2.class).eliminar(rutString)).withRel("eliminar"));
        usuarioModel.add(linkTo(methodOn(UsuarioControllerV2.class).obtenerTodos()).withRel("usuarios"));
        
        return usuarioModel;
    }
}