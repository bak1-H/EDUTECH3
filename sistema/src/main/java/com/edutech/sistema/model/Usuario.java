package com.edutech.sistema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private String rut;
    private String dv;
    private String nombre;
    private String email;
    private LocalDateTime fechaRegistro;
    private Long tipoUsuarioId;
    // Sin cursosIds - se obtienen por pagos

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(rut, usuario.rut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rut);
    }
}