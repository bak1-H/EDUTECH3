//Se Crea TipoUsuarioClient que se encarga de comunicarse con el microservicio de TipoUsuario
// para obtener la información del tipo de usuario asociado a un usuario específico.

package com.edutech.usuario.service;

import com.edutech.usuario.model.TipoUsuarioDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TipoUsuarioClient {

    //Value se usa para inyectar la URL base del servicio de tipo usuario
    //En este caso se define una URL por defecto que apunta a localhost:8081
    //Esto permite que el servicio pueda ser configurado para apuntar a otro servidor
    @Value("${services.tipousuario.url:http://localhost:8081}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public TipoUsuarioDto obtenerTipoUsuario(Long id) {
        try {
            String url = baseUrl + "/api/tipos-usuario/" + id;
            return restTemplate.getForObject(url, TipoUsuarioDto.class);
        } catch (Exception e) {
            return new TipoUsuarioDto(id, "DESCONOCIDO", "Error", "{}", false);
        }
    }
}   