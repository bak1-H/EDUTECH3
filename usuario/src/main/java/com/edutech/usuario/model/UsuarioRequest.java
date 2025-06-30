package com.edutech.usuario.model;

import lombok.Data;

@Data
public class UsuarioRequest {
    private Long rut;
    private String dv;
    private String nombre;
    private String email;
    private String contrasena;
    private Long tipoUsuarioId;
    
    // Información del curso y pago
    private Long cursoId; // ID del curso a asignar
    private Boolean estadoPago; // true = pagado, false = pendiente
}