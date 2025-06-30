package com.edutech.usuario.controller;

import com.edutech.usuario.model.Usuario;
import com.edutech.usuario.model.UsuarioRequest;
import com.edutech.usuario.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones para gestionar usuarios del sistema")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;


    
    // === OBTENER TODOS LOS USUARIOS ===
    @Operation(summary = "Obtener todos los usuarios")
    @Description("Este endpoint permite obtener una lista de todos los usuarios registrados en el sistema.")
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
    @Operation(summary = "Obtener usuario por RUT")
    @Description("Este endpoint permite obtener un usuario específico utilizando su RUT.")
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
    @Operation(summary = "Crear un nuevo usuario")
    @Description("Este endpoint permite crear un nuevo usuario en el sistema. Si el RUT ya existe, se retornará un error 409.")
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
    @Operation(summary = "Actualizar un usuario existente")
    @Description("Este endpoint permite actualizar la información de un usuario existente utilizando su RUT. Si el usuario no existe, se retornará un error 404.")
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
    @Operation(summary = "Eliminar un usuario existente")
    @Description("Este endpoint permite eliminar un usuario del sistema utilizando su RUT. Si el usuario no existe, se retornará un error 404.")
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


}