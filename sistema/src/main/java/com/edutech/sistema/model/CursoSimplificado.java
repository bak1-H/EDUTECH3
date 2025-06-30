package com.edutech.sistema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



//CursoSimplificado es un DTO (Data Transfer Object) que se utiliza para enviar
//información simplificada de un curso a través de la API. Este objeto se utiliza
//para representar la respuesta de un curso simplificado, incluyendo su ID, nombre,
//descripción, ID de pago y estado de pago. Es útil para mostrar información básica


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