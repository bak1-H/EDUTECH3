package com.edutech.sistema.model.enriquecimiento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.edutech.sistema.model.Curso;

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