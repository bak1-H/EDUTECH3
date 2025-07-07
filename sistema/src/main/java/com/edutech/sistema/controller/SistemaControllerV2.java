package com.edutech.sistema.controller;

import com.edutech.sistema.model.enriquecimiento.*;
import com.edutech.sistema.model.Pago;
import com.edutech.sistema.model.Curso;
import com.edutech.sistema.model.Usuario;
import com.edutech.sistema.service.PagoService;
import com.edutech.sistema.service.CursoService;
import com.edutech.sistema.service.EnriquecimientoService;
import com.edutech.sistema.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controlador HATEOAS para Sistema
 * Solo operaciones GET para pagos, cursos y usuarios
 */
@RestController
@RequestMapping("/api/v2/sistema")
@CrossOrigin(origins = "*")
@Tag(name = "Sistema HATEOAS", description = "Operaciones GET con HATEOAS")
public class SistemaControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(SistemaControllerV2.class);

    @Autowired
    private PagoService pagoService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EnriquecimientoService enriquecimientoService;
    // ========== ENDPOINTS DE PAGOS ==========

    @Operation(summary = "Obtener todos los pagos con HATEOAS")
    @GetMapping("/pagos")
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> obtenerTodosPagos() {
        try {
            List<Pago> pagos = pagoService.obtenerTodosLosPagos();
            
            // Validar que la lista no sea null
            if (pagos == null) {
                logger.warn("Lista de pagos es null, devolviendo lista vacía");
                pagos = List.of();
            }
            
            List<EntityModel<Pago>> pagosModel = pagos.stream()
                .filter(pago -> pago != null) // Filtrar pagos null
                .map(this::agregarLinksPago)
                .collect(Collectors.toList());

            CollectionModel<EntityModel<Pago>> collection = CollectionModel.of(pagosModel);
            collection.add(linkTo(methodOn(SistemaControllerV2.class).obtenerTodosPagos()).withSelfRel());
            collection.add(linkTo(methodOn(SistemaControllerV2.class).obtenerTodosCursos()).withRel("cursos"));
            collection.add(linkTo(methodOn(SistemaControllerV2.class).obtenerTodosUsuarios()).withRel("usuarios"));
            
            logger.info("Obtenidos {} pagos con HATEOAS", pagosModel.size());
            return ResponseEntity.ok(collection);
            
        } catch (Exception e) {
            logger.error("Error al obtener todos los pagos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "Obtener pago por ID con HATEOAS")
    @GetMapping("/pagos/{id}")
    public ResponseEntity<EntityModel<Pago>> obtenerPagoPorId(@PathVariable Long id) {
        try {
            // Validar ID
            if (id == null || id <= 0) {
                logger.warn("ID inválido recibido: {}", id);
                return ResponseEntity.badRequest().build();
            }

            Pago pago = pagoService.obtenerPagoPorId(id);
            if (pago != null) {
                EntityModel<Pago> pagoModel = agregarLinksPago(pago);
                logger.info("Pago encontrado: ID {}", id);
                return ResponseEntity.ok(pagoModel);
            } else {
                logger.warn("Pago no encontrado con ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            logger.error("Error al obtener pago por ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ========== ENDPOINTS DE CURSOS ==========

@Operation(summary = "Obtener todos los cursos enriquecidos con usuarios con HATEOAS")
@GetMapping("/cursos")
public ResponseEntity<CollectionModel<EntityModel<CursoEnriquecido>>> obtenerTodosCursos() {
    try {
        List<Curso> cursos = cursoService.obtenerTodosLosCursos();
        
        // Validar que la lista no sea null
        if (cursos == null) {
            logger.warn("Lista de cursos es null, devolviendo lista vacía");
            cursos = List.of();
        }
        
        // Enriquecer cursos con usuarios
        List<EntityModel<CursoEnriquecido>> cursosModel = cursos.stream()
            .filter(curso -> curso != null) // Filtrar cursos null
            .map(curso -> enriquecimientoService.enriquecerCurso(curso)) // Enriquecer con usuarios
            .map(this::agregarLinksCursoEnriquecido) // Usar método específico para curso enriquecido
            .collect(Collectors.toList());

        CollectionModel<EntityModel<CursoEnriquecido>> collection = CollectionModel.of(cursosModel);
        collection.add(linkTo(methodOn(SistemaControllerV2.class).obtenerTodosCursos()).withSelfRel());
        collection.add(linkTo(methodOn(SistemaControllerV2.class).obtenerTodosUsuarios()).withRel("usuarios"));
        
        logger.info("Obtenidos {} cursos enriquecidos con HATEOAS", cursosModel.size());
        return ResponseEntity.ok(collection);
        
    } catch (Exception e) {
        logger.error("Error al obtener todos los cursos enriquecidos: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}


@Operation(summary = "Obtener curso enriquecido por ID con HATEOAS")
@GetMapping("/cursos/{id}")
public ResponseEntity<EntityModel<CursoEnriquecido>> obtenerCursoPorId(@PathVariable Long id) {
    try {
        // Validar ID
        if (id == null || id <= 0) {
            logger.warn("ID de curso inválido: {}", id);
            return ResponseEntity.badRequest().build();
        }

        Curso curso = cursoService.obtenerCursoPorId(id);
        if (curso != null) {
            // Enriquecer el curso con usuarios
            CursoEnriquecido cursoEnriquecido = enriquecimientoService.enriquecerCurso(curso);
            EntityModel<CursoEnriquecido> cursoModel = agregarLinksCursoEnriquecido(cursoEnriquecido);
            logger.info("Curso enriquecido encontrado: ID {}", id);
            return ResponseEntity.ok(cursoModel);
        } else {
            logger.warn("Curso no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        
    } catch (Exception e) {
        logger.error("Error al obtener curso enriquecido por ID {}: {}", id, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

    // ========== ENDPOINTS DE USUARIOS ==========

    @Operation(summary = "Obtener todos los usuarios con HATEOAS")
    @GetMapping("/usuarios")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioEnriquecido>>> obtenerTodosUsuarios() {
        try {
            // Cambia 'obtenerTodosUsuarios' por 'findAll' o el método correcto de tu servicio
            List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
            
            // Validar que la lista no sea null
            if (usuarios == null) {
                logger.warn("Lista de usuarios es null, devolviendo lista vacía");
                usuarios = List.of();
            }
            
            List<EntityModel<UsuarioEnriquecido>> usuariosModel = usuarios.stream()
                .filter(usuario -> usuario != null) // Filtrar usuarios null
                .map(usuario -> enriquecimientoService.enriquecerUsuario(usuario))
                .map(this::agregarLinksUsuarioEnriquecido)
                .collect(Collectors.toList());

            CollectionModel<EntityModel<UsuarioEnriquecido>> collection = CollectionModel.of(usuariosModel);
            collection.add(linkTo(methodOn(SistemaControllerV2.class).obtenerTodosUsuarios()).withSelfRel());
            collection.add(linkTo(methodOn(SistemaControllerV2.class).obtenerTodosPagos()).withRel("pagos"));
            collection.add(linkTo(methodOn(SistemaControllerV2.class).obtenerTodosCursos()).withRel("cursos"));
            
            logger.info("Obtenidos {} usuarios enriquecidos con HATEOAS", usuariosModel.size());
            return ResponseEntity.ok(collection);
            
        } catch (Exception e) {
            logger.error("Error al obtener todos los usuarios: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @Operation(summary = "Obtener usuario por RUT con HATEOAS")
    @GetMapping("/usuarios/{rut}")
    public ResponseEntity<EntityModel<Usuario>> obtenerUsuarioPorRut(@PathVariable String rut) {
        try {
            // Validar RUT
            if (rut == null || rut.trim().isEmpty()) {
                logger.warn("RUT inválido recibido: {}", rut);
                return ResponseEntity.badRequest().build();
            }

            Usuario usuario = usuarioService.obtenerUsuarioPorRut(rut);
            if (usuario != null) {
                EntityModel<Usuario> usuarioModel = agregarLinksUsuario(usuario);
                logger.info("Usuario encontrado: RUT {}", rut);
                return ResponseEntity.ok(usuarioModel);
            } else {
                logger.warn("Usuario no encontrado con RUT: {}", rut);
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            logger.error("Error al obtener usuario por RUT {}: {}", rut, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ========== MÉTODOS PRIVADOS PARA ENLACES HATEOAS ==========

    //Agregar enlaces HATEOAS a Pago
    // Este método agrega enlaces HATEOAS a un objeto Pago, incluyendo enlaces a sí mismo,
    // a la colección completa de pagos, y a los recursos relacionados como usuario y curso.

    private EntityModel<Pago> agregarLinksPago(Pago pago) {
        EntityModel<Pago> pagoModel = EntityModel.of(pago);
        
        try {
            // Validar que el pago tenga ID válido
            if (pago.getId() != null) {
                // Link a sí mismo (self)
                pagoModel.add(linkTo(methodOn(SistemaControllerV2.class)
                    .obtenerPagoPorId(pago.getId())).withSelfRel());
            }
            
            // Link a la colección completa
            pagoModel.add(linkTo(methodOn(SistemaControllerV2.class)
                .obtenerTodosPagos()).withRel("pagos"));
            
            // Links relacionados si existen
            if (pago.getUsuarioRut() != null) {
                pagoModel.add(linkTo(methodOn(SistemaControllerV2.class)
                    .obtenerUsuarioPorRut(String.valueOf(pago.getUsuarioRut()))).withRel("usuario"));
            }
            
            if (pago.getCursoId() != null) {
                pagoModel.add(linkTo(methodOn(SistemaControllerV2.class)
                    .obtenerCursoPorId(pago.getCursoId())).withRel("curso"));
            }
            
        } catch (Exception e) {
            logger.error("Error creando links HATEOAS para pago {}: {}", 
                        pago.getId(), e.getMessage());
        }

        return pagoModel;
    }

    
     //Agregar enlaces HATEOAS a Curso
     // Este método agrega enlaces HATEOAS a un objeto Curso, incluyendo enlaces a sí mismo,
     // a la colección completa de cursos, y a los recursos relacionados como pagos y usuarios.

    private EntityModel<Curso> agregarLinksCurso(Curso curso) {
        EntityModel<Curso> cursoModel = EntityModel.of(curso);
        
        try {
            // Validar que el curso tenga ID válido
            if (curso.getId() != null) {
                // Link a sí mismo (self)
                cursoModel.add(linkTo(methodOn(SistemaControllerV2.class)
                    .obtenerCursoPorId(curso.getId())).withSelfRel());
            }
            
            // Link a la colección completa
            cursoModel.add(linkTo(methodOn(SistemaControllerV2.class)
                .obtenerTodosCursos()).withRel("cursos"));
            
            // Links de navegación
            cursoModel.add(linkTo(methodOn(SistemaControllerV2.class)
                .obtenerTodosPagos()).withRel("pagos"));
            cursoModel.add(linkTo(methodOn(SistemaControllerV2.class)
                .obtenerTodosUsuarios()).withRel("usuarios"));
            
        } catch (Exception e) {
            logger.error("Error creando links HATEOAS para curso {}: {}", 
                        curso.getId(), e.getMessage());
        }

        return cursoModel;
    }

    
     //Agregar enlaces HATEOAS a Usuario
     // Este método agrega enlaces HATEOAS a un objeto Usuario, incluyendo enlaces a sí mismo,
     // a la colección completa de usuarios, y a los recursos relacionados como pagos y cursos.
     
    private EntityModel<Usuario> agregarLinksUsuario(Usuario usuario) {
        EntityModel<Usuario> usuarioModel = EntityModel.of(usuario);
        
        try {
            // Validar que el usuario tenga RUT válido
            if (usuario.getRut() != null && !usuario.getRut().trim().isEmpty()) {
                // Link a sí mismo (self) - CORREGIDO: getRut() ya es String
                usuarioModel.add(linkTo(methodOn(SistemaControllerV2.class)
                    .obtenerUsuarioPorRut(usuario.getRut())).withSelfRel());
            }
            
            // Link a la colección completa
            usuarioModel.add(linkTo(methodOn(SistemaControllerV2.class)
                .obtenerTodosUsuarios()).withRel("usuarios"));
            
            // Links de navegación
            usuarioModel.add(linkTo(methodOn(SistemaControllerV2.class)
                .obtenerTodosPagos()).withRel("pagos"));
            usuarioModel.add(linkTo(methodOn(SistemaControllerV2.class)
                .obtenerTodosCursos()).withRel("cursos"));
            
        } catch (Exception e) {
            logger.error("Error creando links HATEOAS para usuario {}: {}", 
                        usuario.getRut(), e.getMessage());
        }

        return usuarioModel;
    }


    // Método privado para agregar enlaces HATEOAS a UsuarioEnriquecido
    private EntityModel<UsuarioEnriquecido> agregarLinksUsuarioEnriquecido(UsuarioEnriquecido usuarioEnriquecido) {
        EntityModel<UsuarioEnriquecido> usuarioModel = EntityModel.of(usuarioEnriquecido);
        try {
            if (usuarioEnriquecido.getRut() != null && !usuarioEnriquecido.getRut().trim().isEmpty()) {
                usuarioModel.add(linkTo(methodOn(SistemaControllerV2.class)
                    .obtenerUsuarioPorRut(usuarioEnriquecido.getRut())).withSelfRel());
            }
            usuarioModel.add(linkTo(methodOn(SistemaControllerV2.class)
                .obtenerTodosUsuarios()).withRel("usuarios"));
            usuarioModel.add(linkTo(methodOn(SistemaControllerV2.class)
                .obtenerTodosPagos()).withRel("pagos"));
            usuarioModel.add(linkTo(methodOn(SistemaControllerV2.class)
                .obtenerTodosCursos()).withRel("cursos"));
        } catch (Exception e) {
            logger.error("Error creando links HATEOAS para usuario enriquecido {}: {}", 
                        usuarioEnriquecido.getRut(), e.getMessage());
        }
        return usuarioModel;
    }


    //Agregar enlaces HATEOAS a Curso Enriquecido
    // Este método agrega enlaces HATEOAS a un objeto CursoEnriquecido, incluyendo enlaces a sí mismo,
    // a la colección completa de cursos, y a los recursos relacionados como usuarios y pagos.

private EntityModel<CursoEnriquecido> agregarLinksCursoEnriquecido(CursoEnriquecido curso) {
    EntityModel<CursoEnriquecido> cursoModel = EntityModel.of(curso);
    
    try {
        // Validar que el curso tenga ID válido
        if (curso.getId() != null && curso.getId() > 0) {
            // Link a sí mismo (self)
            cursoModel.add(linkTo(methodOn(SistemaControllerV2.class)
                .obtenerCursoPorId(curso.getId())).withSelfRel());
        }
        
        // Link a la colección completa
        cursoModel.add(linkTo(methodOn(SistemaControllerV2.class)
            .obtenerTodosCursos()).withRel("cursos"));
        
        // Links de navegación
        cursoModel.add(linkTo(methodOn(SistemaControllerV2.class)
            .obtenerTodosPagos()).withRel("pagos"));
        cursoModel.add(linkTo(methodOn(SistemaControllerV2.class)
            .obtenerTodosUsuarios()).withRel("usuarios"));
        
        // Links a usuarios específicos si existen
        if (curso.getUsuariosInscritos() != null && !curso.getUsuariosInscritos().isEmpty()) {
            for (Usuario usuario : curso.getUsuariosInscritos()) {
                if (usuario.getRut() != null && !usuario.getRut().trim().isEmpty()) {
                    cursoModel.add(linkTo(methodOn(SistemaControllerV2.class)
                        .obtenerUsuarioPorRut(usuario.getRut())).withRel("usuario-" + usuario.getRut()));
                }
            }
        }
        
    } catch (Exception e) {
        logger.error("Error creando links HATEOAS para curso enriquecido {}: {}", 
                    curso.getId(), e.getMessage());
    }

    return cursoModel;
}
}