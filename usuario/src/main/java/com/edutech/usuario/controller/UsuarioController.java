package com.edutech.usuario.controller;

import com.edutech.usuario.model.Usuario;
import com.edutech.usuario.model.UsuarioRequest;
import com.edutech.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    // === OBTENER TODOS LOS USUARIOS ===
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        try {
            List<Usuario> usuarios = usuarioService.obtenerTodos();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            logger.error("Error al obtener todos los usuarios: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // === OBTENER USUARIO POR RUT ===
    @GetMapping("/{rut}")
    public ResponseEntity<Usuario> obtenerPorRut(@PathVariable String rut) {
        try {
            Usuario usuario = usuarioService.obtenerPorRut(rut);
            if (usuario != null) {
                return ResponseEntity.ok(usuario);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            logger.error("Error de validación al obtener usuario {}: {}", rut, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error interno al obtener usuario {}: {}", rut, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // === CREAR USUARIO ===
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody UsuarioRequest usuarioRequest) {
        try {
            // Verificar si el RUT ya existe
            Usuario usuarioExistente = usuarioService.obtenerPorRut(usuarioRequest.getRut().toString());
            if (usuarioExistente != null) {
                logger.warn("Intento de crear usuario con RUT duplicado: {}", usuarioRequest.getRut());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            
            Usuario usuarioCreado = usuarioService.crearUsuario(usuarioRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
            
        } catch (RuntimeException e) {
            logger.error("Error de validación al crear usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Error interno al crear usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // === ACTUALIZAR USUARIO ===
    @PutMapping("/{rut}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable String rut, @RequestBody UsuarioRequest usuarioRequest) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(rut, usuarioRequest);
            if (usuarioActualizado != null) {
                return ResponseEntity.ok(usuarioActualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            logger.error("Error de validación al actualizar usuario {}: {}", rut, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Error interno al actualizar usuario {}: {}", rut, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // === ELIMINAR USUARIO ===
    @DeleteMapping("/{rut}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String rut) {
        try {
            boolean eliminado = usuarioService.eliminarUsuario(rut);
            if (eliminado) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            logger.error("Error de validación al eliminar usuario {}: {}", rut, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            logger.error("Error interno al eliminar usuario {}: {}", rut, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // === VERIFICAR SI RUT EXISTE ===
    @GetMapping("/existe/{rut}")
    public ResponseEntity<Boolean> existeRut(@PathVariable String rut) {
        try {
            Usuario usuario = usuarioService.obtenerPorRut(rut);
            boolean existe = (usuario != null);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            logger.error("Error al verificar existencia de RUT {}: {}", rut, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    // === OBTENER POR TIPO DE USUARIO ===
    @GetMapping("/tipo/{tipoUsuarioId}")
    public ResponseEntity<List<Usuario>> obtenerPorTipoUsuario(@PathVariable Long tipoUsuarioId) {
        try {
            List<Usuario> usuarios = usuarioService.obtenerPorTipoUsuario(tipoUsuarioId);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            logger.error("Error al obtener usuarios por tipo {}: {}", tipoUsuarioId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // === OBTENER POR EMAIL ===
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerPorEmail(@PathVariable String email) {
        try {
            Usuario usuario = usuarioService.obtenerPorEmail(email);
            if (usuario != null) {
                return ResponseEntity.ok(usuario);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error al obtener usuario por email {}: {}", email, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}