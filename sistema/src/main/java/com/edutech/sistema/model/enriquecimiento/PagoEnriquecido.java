package com.edutech.sistema.model.enriquecimiento;

import com.edutech.sistema.model.Curso;
import com.edutech.sistema.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


//PagoEnriquecido es un DTO (Data Transfer Object) que se utiliza para enviar
// información enriquecida de un pago a través de la API. Este objeto se utiliza    


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoEnriquecido {
    private Long id;
    private boolean estado;
    private Long usuarioRut;
    private Long cursoId;
    
    // Datos enriquecidos
    private Usuario usuario;
    private Curso curso;
}