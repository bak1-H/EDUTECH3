package com.edutech.pago.service;

import com.edutech.pago.model.Pago;
import com.edutech.pago.repository.PagoRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public List<Pago> obtenerTodos() {
        return pagoRepository.findAll();
    }

    public Optional<Pago> obtenerPorId(Long id) {
        return pagoRepository.findById(id);
    }

    public List<Pago> obtenerPorUsuarioRut(Long usuarioRut) {
        return pagoRepository.findByUsuarioRut(usuarioRut);
    }

    public List<Pago> obtenerPorCursoId(Long cursoId) {
        return pagoRepository.findByCursoId(cursoId);
    }

    public void eliminarPago(Long id) {
        pagoRepository.deleteById(id);
    }

    public Pago guardarPago(Pago pago) {
        // Validar que el curso existe
        if (pago.getCursoId() == null || pago.getCursoId() <= 0) {
            throw new RuntimeException("El curso debe tener un ID válido");
        }
        
        // Validar que el usuario existe
        if (pago.getUsuarioRut() == null || pago.getUsuarioRut() <= 0) {
            throw new RuntimeException("El usuario debe tener un RUT válido");
        }

        return pagoRepository.save(pago);
    }

    public Pago actualizarEstado(Long id, boolean estado) {
        Optional<Pago> pagoOpt = pagoRepository.findById(id);
        if (pagoOpt.isPresent()) {
            Pago pago = pagoOpt.get();
            pago.setEstado(estado);
            return pagoRepository.save(pago);
        } else {
            throw new RuntimeException("Pago no encontrado con ID: " + id);
        }
    }

}