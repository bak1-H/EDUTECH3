package com.duoc.Fullstack3.controllers;

import com.duoc.Fullstack3.models.TipoUsuario;
import com.duoc.Fullstack3.services.TipoUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-usuario")
@CrossOrigin(origins = "*")
public class TipoUsuarioController {

    @Autowired
    private TipoUsuarioService tipoUsuarioService;

    // Solo endpoints de consulta - NO CRUD
    @GetMapping
    public List<TipoUsuario> obtenerTodos() {
        return tipoUsuarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoUsuario> obtenerPorId(@PathVariable Long id) {
        return tipoUsuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}