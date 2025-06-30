package com.edutech.sistema.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    private Long id;
    private boolean estado;
    private Long usuarioRut;
    private Long cursoId;
}