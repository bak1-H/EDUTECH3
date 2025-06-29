//Controller para manejar las operaciones CRUD de los usuarios
// Este controlador expone endpoints para crear, leer, actualizar y eliminar usuarios
// Además, permite consultar usuarios por su RUT y obtener todos los usuarios registrados



package com.edutech.usuario.controller;
import com.edutech.usuario.model.Usuario;
import com.edutech.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/{rut}")
    public ResponseEntity<Usuario> obtenerPorRut(@PathVariable Long rut) {
        return usuarioService.obtenerPorRut(rut)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        return usuarioService.guardarUsuario(usuario);
    }

    @PutMapping("/{rut}")
    public Usuario actualizar(@PathVariable Long rut, @RequestBody Usuario usuario) {
        usuario.setRut(rut);
        return usuarioService.guardarUsuario(usuario);
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long rut) {
        usuarioService.eliminarUsuario(rut);
        return ResponseEntity.ok("El Usuario ha sido eliminado correctamente.");
    }
}