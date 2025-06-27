package com.edutech.pago.repository;

import com.edutech.pago.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByUsuarioRut(Long usuarioRut);
    List<Pago> findByCursoId(Long cursoId);
}