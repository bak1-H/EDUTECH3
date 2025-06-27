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

    public Pago guardarPago(Pago pago) {
        return pagoRepository.save(pago);
    }

    public void eliminarPago(Long id) {
        pagoRepository.deleteById(id);
    }
}