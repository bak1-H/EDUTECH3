package com.edutech.pago.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "estado", nullable = false)
    private boolean estado;
    
    @Column(name = "usuario_rut", nullable = false)
    private Long usuarioRut;
    
    @Column(name = "nombre_usuario")
    private String nombreUsuario; // Nuevo campo
    
    @Column(name = "curso_id", nullable = false)
    private Long cursoId;
    
    @Column(name = "nombre_curso")
    private String nombreCurso; // Nuevo campo
}