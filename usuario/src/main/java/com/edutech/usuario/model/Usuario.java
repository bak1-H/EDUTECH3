package com.edutech.usuario.model;
import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario{
    @Id
    @Column(name = "rut", nullable = false)
    private Long rut;

    @Column(name = "dv", nullable = false)
    private String dv;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Column(name = "fecha_registro", nullable = false)
    private Timestamp fechaRegistro;

    @Column(name = "tipo_usuario_id", nullable = false)
    private Long tipoUsuarioId;

    @Transient
    private TipoUsuarioDto tipoUsuario;
}