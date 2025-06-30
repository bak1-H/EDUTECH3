package com.edutech.sistema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Objects;



// Usuario es un DTO (Data Transfer Object) que se utiliza para enviar
// información de un usuario a través de la API. Este objeto se utiliza para
// representar la respuesta de un usuario, incluyendo su RUT, DV, nombre, email,
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


    // Este campo se utiliza para identificar el tipo de usuario (por ejemplo, estudiante, profesor, etc.)
    // Se utiliza el ID del tipo de usuario en lugar de un objeto enriquecido para evitar
    // la dependencia del microservicio de tipos de usuario. Esto permite que el sistema
    // funcione de manera más independiente y eficiente, ya que no es necesario realizar
    // una llamada al microservicio de tipos de usuario para obtener información adicional.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(rut, usuario.rut);
    }

    // El método hashCode se utiliza para generar un código hash único para el objeto Usuario
    // basado en su RUT. Esto es útil para almacenar el objeto en colecciones como
    // HashMap o HashSet, donde se requiere un código hash para identificar de manera eficiente
    // los objetos. Al implementar este método, se asegura que dos objetos Usuario con el mismo
    // RUT generen el mismo código hash, lo que permite una comparación eficiente y evita
    // duplicados en colecciones que utilizan hashing.
    @Override
    public int hashCode() {
        return Objects.hash(rut);
    }
}