package com.edutech.pago.controller;

import com.edutech.pago.model.Pago;
import com.edutech.pago.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public List<Pago> obtenerTodos() {
        return pagoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Optional<Pago> obtenerPorId(@PathVariable Long id) {
        return pagoService.obtenerPorId(id);
    }

    @GetMapping("/usuario/{usuarioRut}")
    public List<Pago> obtenerPorUsuarioRut(@PathVariable Long usuarioRut) {
        return pagoService.obtenerPorUsuarioRut(usuarioRut);
    }

    @GetMapping("/curso/{cursoId}")
    public List<Pago> obtenerPorCursoId(@PathVariable Long cursoId) {
        return pagoService.obtenerPorCursoId(cursoId);
    }

    @PostMapping
    public Pago guardarPago(@RequestBody Pago pago) {
        return pagoService.guardarPago(pago);
    }

    @DeleteMapping("/{id}")
    public void eliminarPago(@PathVariable Long id) {
        pagoService.eliminarPago(id);
    }
}