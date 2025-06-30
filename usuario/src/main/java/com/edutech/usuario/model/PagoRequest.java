package com.edutech.usuario.model;

import lombok.Data;



//PagoRequest sirve en la logica en el microservicio de usuario
// para crear un pago enriquecido con datos reales del usuario y del curso.
// Este objeto se utiliza para enviar la información necesaria al microservicio de pago
// y registrar el pago de un usuario por un curso específico.

@Data
public class PagoRequest {
    private Long usuarioRut;
    private String nombreUsuario;
    private Long cursoId;
    private String nombreCurso;
    private boolean estado; // true = pagado, false = pendiente
} 
    

