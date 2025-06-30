package com.edutech.usuario.model;

import lombok.Data;



//UsuarioRequest es un DTO (Data Transfer Object) que se utiliza para recibir
// información de un usuario a través de la API. Este objeto se utiliza para
// representar la solicitud de creación o actualización de un usuario, incluyendo
// su RUT, DV, nombre, email, contraseña, tipo de usuario y la información del curso
// y pago. Es útil para validar y transferir datos entre la interfaz de usuario

@Data
public class UsuarioRequest {
    private Long rut;
    private String dv;
    private String nombre;
    private String email;
    private String contrasena;
    private Long tipoUsuarioId;
    
    // Información del curso y pago
    private Long cursoId;
    private Boolean estadoPago; // true = pagado, false = pendiente
}