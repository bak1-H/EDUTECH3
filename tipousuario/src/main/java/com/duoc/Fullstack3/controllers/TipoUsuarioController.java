package com.duoc.Fullstack3.controllers;

import com.duoc.Fullstack3.models.TipoUsuario;
import com.duoc.Fullstack3.services.TipoUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-usuario")
@Tag(name = "Tipos de Usuario", description = "Operaciones GET para consultar tipos de usuario")
public class TipoUsuarioController {

    @Autowired
    private TipoUsuarioService tipoUsuarioService;

    // Solo endpoints de consulta - NO CRUD
    @Operation(summary = "Obtener todos los tipos de usuario", description = "Devuelve una lista de todos los tipos de usuario registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de tipos de usuario obtenida correctamente")
    @ApiResponse(responseCode = "404", description = "No se encontraron tipos de usuario")
    @GetMapping
    public List<TipoUsuario> obtenerTodos() {
        return tipoUsuarioService.obtenerTodos();
    }
    @Operation(summary = "Obtener tipo de usuario por ID", description = "Devuelve un tipo de usuario específico basado en su ID.")
    @ApiResponse(responseCode = "200", description = "Tipo de usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Tipo de usuario no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<TipoUsuario> obtenerPorId(@PathVariable Long id) {
        return tipoUsuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}