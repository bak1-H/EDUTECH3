package com.edutech.sistema.model.enriquecimiento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.edutech.sistema.model.Curso;


//UsuarioEnriquecido es un DTO (Data Transfer Object) que se utiliza para enviar
//información enriquecida de un usuario a través de la API. Este objeto se utiliza
//para representar la respuesta de un usuario enriquecido, incluyendo su RUT, nombre,
//email y una lista de cursos agregados. Es útil para mostrar información del usuario


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEnriquecido {
    private String rut;
    private String nombre;
    private String email;
    
    // Usar CursoSimplificado en lugar de Curso completo
    private List<Curso> cursosAgregados;
}