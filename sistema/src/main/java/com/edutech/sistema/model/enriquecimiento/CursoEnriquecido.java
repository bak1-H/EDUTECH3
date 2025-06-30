package com.edutech.sistema.model.enriquecimiento;

import com.edutech.sistema.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


//CursoEnriquecido es un DTO (Data Transfer Object) que se utiliza para enviar
// información enriquecida de un curso a través de la API. Este objeto se utiliza



@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoEnriquecido {
    private Long id;
    private String nombre;
    private String descripcion;
    
    // Datos enriquecidos
    private List<Usuario> usuariosInscritos;
    private List<PagoEnriquecido> pagosRelacionados;
    private int totalInscritos;
}