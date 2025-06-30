package com.edutech.usuario.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UsuarioResponse {
    private Long rut;
    private String dv;
    private String nombre;
    private String email;
    private LocalDateTime fechaRegistro;
    private Long tipoUsuarioId; // Solo ID, sin enriquecimiento
    private String mensaje; // Para mostrar información sobre el pago creado
}