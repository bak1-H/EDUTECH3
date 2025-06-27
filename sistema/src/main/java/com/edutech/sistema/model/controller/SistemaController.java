package com.edutech.sistema.model.controller;

import com.edutech.sistema.model.Pago;
import com.edutech.sistema.service.PagoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/sistema")
public class SistemaController {

    private final PagoService pagoService;

    public SistemaController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping("/pago/{id}")
    public Pago getPago(@PathVariable Long id) {
        return pagoService.obtenerPagoPorId(id);
    }
    @GetMapping("/cursos/{id}")
    public Pago getCurso(@PathVariable Long id) {
        return pagoService.obtenerPagoPorId(id);
    }
    @GetMapping("/usuario/{id}")
    public Pago getUsuario(@RequestParam Long id) {
        return pagoService.obtenerPagoPorId(id);
    }

    
}