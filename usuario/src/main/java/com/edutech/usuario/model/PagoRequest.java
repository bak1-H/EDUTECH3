package com.edutech.usuario.model;

import lombok.Data;

@Data
public class PagoRequest {
    private Long usuarioRut;
    private String nombreUsuario;
    private Long cursoId;
    private String nombreCurso;
    private boolean estado; // true = pagado, false = pendiente
} 
    

