package com.edutech.sistema.model.controller;
import com.edutech.sistema.model.Pago;
import com.edutech.sistema.model.Curso;
import com.edutech.sistema.model.Usuario;
import com.edutech.sistema.service.PagoService;
import com.edutech.sistema.service.CursoService;
import com.edutech.sistema.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sistema")
public class SistemaController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/pagos/{id}")
    public Pago getPago(@PathVariable Long id) {
        return pagoService.obtenerPagoPorId(id);
    }

    @GetMapping("/cursos/{id}")
    public Curso getCurso(@PathVariable Long id) {
        return cursoService.obtenerCursoPorId(id);
    }

    @GetMapping("/usuarios/{rut}")
    public Usuario getUsuario(@PathVariable String rut) {
        return usuarioService.obtenerUsuarioPorRut(rut);
    }

    @GetMapping("/pagos")
    public List<Pago> getAllPagos() {
        return pagoService.obtenerTodosLosPagos();
    }

    @GetMapping("/cursos")
    public List<Curso> getAllCursos() {
        return cursoService.obtenerTodosLosCursos();
    }

    @GetMapping("/usuarios")
    public List<Usuario> getAllUsuarios() {
        return usuarioService.obtenerTodosLosUsuarios();
    }
}