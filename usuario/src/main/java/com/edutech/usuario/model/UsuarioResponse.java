package com.edutech.usuario.model;

import lombok.Data;
import java.time.LocalDateTime;



// UsuarioResponse es un DTO (Data Transfer Object) que se utiliza para enviar
// información de un usuario a través de la API. Este objeto se utiliza para
// representar la respuesta de un usuario, incluyendo su RUT, DV, nombre, email,
// fecha de registro, tipo de usuario y un mensaje opcional. Es útil para
// mostrar información del usuario en la interfaz de usuario o en respuestas
// de API.
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