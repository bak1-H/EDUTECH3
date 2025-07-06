package com.edutech.sistema.controller;
import com.edutech.sistema.model.Pago;
import com.edutech.sistema.model.Curso;
import com.edutech.sistema.model.Usuario;
import com.edutech.sistema.model.enriquecimiento.CursoEnriquecido;
import com.edutech.sistema.model.enriquecimiento.PagoEnriquecido;
import com.edutech.sistema.model.enriquecimiento.UsuarioEnriquecido;
import com.edutech.sistema.service.PagoService;
import com.edutech.sistema.service.CursoService;
import com.edutech.sistema.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.edutech.sistema.service.EnriquecimientoService;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/sistema/")
@Tag(name = "Sistema", description = "Operaciones para consultar pagos, cursos y usuarios")
public class SistemaController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EnriquecimientoService enriquecimientoService;

    // ENDPOINTS DE PAGOS
    @Operation(summary = "Obtener todos los pagos enriquecidos", description = "Devuelve una lista de todos los pagos con información de usuario y curso.")
    @ApiResponse(responseCode = "200", description = "Lista de pagos enriquecidos obtenida correctamente")
    @GetMapping("pagos")
    public List<PagoEnriquecido> getAllPagos() {
        List<Pago> pagos = pagoService.obtenerTodosLosPagos();
        return enriquecimientoService.enriquecerPagos(pagos);
    }

    @Operation(summary = "Obtener pago enriquecido por ID", description = "Devuelve un pago específico con información de usuario y curso.")
    @ApiResponse(responseCode = "200", description = "Pago enriquecido encontrado")
    @GetMapping("pagos/{id}")
    public PagoEnriquecido getPago(@PathVariable Long id) {
        Pago pago = pagoService.obtenerPagoPorId(id);
        return pago != null ? enriquecimientoService.enriquecerPago(pago) : null;
    }

    // ENDPOINTS DE CURSOS
    @Operation(summary = "Obtener todos los cursos enriquecidos",
     description = "Devuelve una lista de todos los cursos con información de usuarios inscritos.")

    @ApiResponse(responseCode = "200", description = "Lista de cursos enriquecidos obtenida correctamente")
    
    @GetMapping("cursos")
    public List<CursoEnriquecido> getAllCursos() {
        List<Curso> cursos = cursoService.obtenerTodosLosCursos();
        return enriquecimientoService.enriquecerCursos(cursos);
    }

    @Operation(summary = "Obtener curso enriquecido por ID", description = "Devuelve un curso específico con información de usuarios inscritos.")
    @ApiResponse(responseCode = "200", description = "Curso enriquecido encontrado")
    @GetMapping("cursos/{id}")
    public CursoEnriquecido getCurso(@PathVariable Long id) {
        return enriquecimientoService.enriquecerCursoPorId(id);
    }

    // ENDPOINTS DE USUARIOS
    @Operation(summary = "Obtener todos los usuarios enriquecidos", description = "Devuelve una lista de todos los usuarios con cursos agregados.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios enriquecidos obtenida correctamente")
    @GetMapping("usuarios")
    public List<UsuarioEnriquecido> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return enriquecimientoService.enriquecerUsuarios(usuarios);
    }

    @Operation(summary = "Obtener usuario enriquecido por RUT", description = "Devuelve un usuario específico con cursos agregados.")
    @ApiResponse(responseCode = "200", description = "Usuario enriquecido encontrado")
    @GetMapping("usuarios/{rut}")
    public UsuarioEnriquecido getUsuario(@PathVariable String rut) {
        return enriquecimientoService.enriquecerUsuarioPorRut(rut);
    }
}