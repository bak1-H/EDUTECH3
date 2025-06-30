//Acá se define un DTO (Data Transfer Object) para representar los tipos de usuario
// que se utilizará para transferir datos entre el microservicio de usuario y el de tipo usuario.
package com.edutech.usuario.model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoUsuarioDto {
    private Long id;
    private String nombre;
    private String descripcion;
    private String permisos;
    private Boolean activo;
}