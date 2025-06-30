package com.edutech.sistema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoSimplificado {
    private Long id; // Cambiado de cursoId a id para consistencia
    private String nombreCurso;
    private String descripcionCurso;
    private Long pagoId;
    private boolean pagoPagado;
}