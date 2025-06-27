package com.edutech.pago.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Entity
@Table(name = "pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioRut; // Relaciona con Usuario (rut)

    @Column(nullable = false)
    private Long cursoId; // Relaciona con Curso (id)

    @Column(nullable = false)
    private boolean estado; // Estado del pago
}