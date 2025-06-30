package com.edutech.pago.model;
import lombok.Data;


/**
 * Usuario es una clase que representa un usuario en el sistema.
 * Contiene información básica del usuario como su RUT y nombre.
 * Esta clase se utiliza para transferir datos entre diferentes capas de la aplicación.
 */
@Data
public class Usuario {
    private String rut;
    private String nombre;
}