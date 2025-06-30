package com.duoc.Fullstack3.controllers;

import com.duoc.Fullstack3.models.TipoUsuario;
import com.duoc.Fullstack3.services.TipoUsuarioService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controlador HATEOAS para TipoUsuario
 * Solo operaciones GET (obtener todos y por ID)
 */
@RestController
@RequestMapping("/api/v2/tipos-usuario")
@Tag(name = "Tipos de Usuario con HATEOAS", 
     description = "Operaciones GET co HATEOAS")
public class TipoUsuarioControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(TipoUsuarioControllerV2.class);

    @Autowired
    private TipoUsuarioService tipoUsuarioService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<TipoUsuario>>> obtenerTodos() {
        try {
            // Obtener datos del servicio
            List<TipoUsuario> tipos = tipoUsuarioService.obtenerTodos();
            
            // Convertir cada TipoUsuario a EntityModel con links
            List<EntityModel<TipoUsuario>> tiposModel = tipos.stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());

            // Crear colección con link a sí misma
            CollectionModel<EntityModel<TipoUsuario>> collection = CollectionModel.of(tiposModel);
            // Agregar enlace a la colección completa
            collection.add(linkTo(methodOn(TipoUsuarioControllerV2.class).obtenerTodos()).withSelfRel());
            // logger.info sirve para registrar información en la consola
            // y es útil para depurar y monitorear el comportamiento de la aplicación
            logger.info("Obtenidos {} tipos de usuario con HATEOAS", tipos.size());
            // Retornar la colección con estado 200 OK
            // ResponseEntity.ok es una forma de construir una respuesta HTTP 200 OK
            return ResponseEntity.ok(collection);


        } catch (Exception e) {
            logger.error("Error al obtener todos los tipos de usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<TipoUsuario>> obtenerPorId(@PathVariable Long id) {
        try {
            // esta linea sirve para validar el ID recibido
            // Si el ID es nulo o menor o igual a 0, retorna un error
            if (id == null || id <= 0) {
                logger.warn("ID inválido recibido: {}", id);
                return ResponseEntity.badRequest().build();
            }

            // Buscar tipo de usuario
            Optional<TipoUsuario> tipoOpt = tipoUsuarioService.obtenerPorId(id);
            if (tipoOpt.isPresent()) {
                EntityModel<TipoUsuario> tipoModel = agregarLinks(tipoOpt.get());
                logger.info("Tipo de usuario encontrado: {} (ID: {})", tipoOpt.get().getNombre(), id);
                return ResponseEntity.ok(tipoModel);
            } else {
                logger.warn("Tipo de usuario no encontrado con ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            logger.error("Error al obtener tipo de usuario por ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * Método privado para agregar enlaces HATEOAS a cada TipoUsuario
     * Links incluidos:
     * - self: Link al propio recurso
     * - tipos-usuario: Link a la colección completa
     * - activos: Link a tipos activos
     * - verificar: Link para verificar existencia
     */
    private EntityModel<TipoUsuario> agregarLinks(TipoUsuario tipoUsuario) {
        EntityModel<TipoUsuario> tipoModel = EntityModel.of(tipoUsuario);
        
        try {
            // Link a sí mismo (self)
            tipoModel.add(linkTo(methodOn(TipoUsuarioControllerV2.class)
                .obtenerPorId(tipoUsuario.getId())).withSelfRel());
            
            // Link a la colección completa
            tipoModel.add(linkTo(methodOn(TipoUsuarioControllerV2.class)
                .obtenerTodos()).withRel("tipos-usuario"));
            
        } catch (Exception e) {
            logger.error("Error creando links HATEOAS para tipo de usuario {}: {}", 
                        tipoUsuario.getId(), e.getMessage());
        }

        return tipoModel;
    }
}