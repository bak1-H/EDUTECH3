package com.edutech.sistema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



// Curso es un DTO (Data Transfer Object) que se utiliza para enviar
// información de un curso a través de la API. Este objeto se utiliza para
// representar la respuesta de un curso, incluyendo su ID, nombre, descripción,
// y un constructor adicional para compatibilidad. Es útil para mostrar información
// del curso en la interfaz de usuario o en respuestas de API. El constructor adicional
// permite crear un objeto Curso con solo el ID y el nombre del curso, lo que es
// útil en situaciones donde no se requiere la descripción completa del curso.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    private Long id;
    private String nombreCurso;
    private String descripcionCurso;
    
    // Constructor adicional para compatibilidad
    public Curso(Long id, String nombreCurso) {
        this.id = id;
        this.nombreCurso = nombreCurso;
    }
}