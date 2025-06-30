package com.edutech.pago.controller;

import com.edutech.pago.model.Pago;
import com.edutech.pago.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagos")
@Tag(name = "Pagos", description = "Operaciones CRUD para pagos")
public class PagoController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PagoController.class);

    @Autowired
    private PagoService pagoService;

    @Operation(summary = "Obtener todos los pagos")
    @GetMapping
    public List<Pago> obtenerTodos() {
        return pagoService.obtenerTodos();
    }

    @Operation(summary = "Crear un nuevo pago")
    @ApiResponse(responseCode = "201", description = "Pago creado correctamente")
    @PostMapping
    public ResponseEntity<Pago> crearPago(@RequestBody Pago pago) {
        logger.info("Recibiendo pago: Usuario={} ({}), Curso={} ({})", 
                pago.getUsuarioRut(), pago.getNombreUsuario(), 
                pago.getCursoId(), pago.getNombreCurso());
        
        Pago pagoCreado = pagoService.guardarPago(pago);
        return ResponseEntity.ok(pagoCreado);
}
        
    @Operation(summary = "Obtener pago por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPorId(@PathVariable Long id) {
        Optional<Pago> pago = pagoService.obtenerPorId(id);
        return pago.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener pagos por RUT de usuario")
    @GetMapping("/usuario/{usuarioRut}")
    public List<Pago> obtenerPorUsuarioRut(@PathVariable Long usuarioRut) {
        return pagoService.obtenerPorUsuarioRut(usuarioRut);
    }

    @Operation(summary = "Obtener pagos por ID de curso")
    @GetMapping("/curso/{cursoId}")
    public List<Pago> obtenerPorCursoId(@PathVariable Long cursoId) {
        return pagoService.obtenerPorCursoId(cursoId);
    }

    @Operation(summary = "Eliminar Pago")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Actualizar estado de pago")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pago> actualizarEstado(@PathVariable Long id, @RequestParam boolean
estado) {
        try {
            Pago pagoActualizado = pagoService.actualizarEstado(id, estado);
            return ResponseEntity.ok(pagoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}