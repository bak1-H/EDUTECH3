package com.edutech.sistema.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// Pago es un DTO (Data Transfer Object) que se utiliza para enviar
// información de un pago a través de la API. Este objeto se utiliza para
// representar la respuesta de un pago, incluyendo su ID, estado (pagado o pendiente),
// el RUT del usuario que realizó el pago y el ID del curso asociado al pago.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    private Long id;
    private boolean estado;
    private Long usuarioRut;
    private Long cursoId;
}