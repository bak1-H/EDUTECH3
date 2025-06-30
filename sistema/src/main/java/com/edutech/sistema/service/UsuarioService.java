package com.edutech.sistema.service;

import com.edutech.sistema.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


//UsuarioService es un servicio que se encarga de interactuar con el microservicio de usuarios
// para obtener información sobre usuarios. Este servicio utiliza RestTemplate para realizar
// solicitudes HTTP al microservicio de usuarios y manejar las respuestas. Proporciona métodos
// para obtener un usuario por su RUT y para obtener todos los usuarios disponibles. Además,
// maneja excepciones específicas como HttpClientErrorException.NotFound para registrar advertencias
// y otros errores para registrar mensajes de error. 
@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private RestTemplate restTemplate;

    public Usuario obtenerUsuarioPorRut(String rut) {
        try {
            String url = "http://localhost:8081/api/usuarios/" + rut;
            logger.info("Llamando a URL: {}", url);
            
            Usuario usuario = restTemplate.getForObject(url, Usuario.class);
            
            if (usuario != null) {
                logger.info("Usuario obtenido exitosamente - RUT: {}, Nombre: {}", 
                          usuario.getRut(), usuario.getNombre());
            } else {
                logger.warn("Usuario retornado es null para RUT: {}", rut);
            }
            
            return usuario;
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Usuario con RUT {} no encontrado - 404", rut);
            return null;
        } catch (Exception e) {
            logger.error("Error al obtener usuario con RUT {}: {}", rut, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        try {
            String url = "http://localhost:8081/api/usuarios";
            logger.info("Llamando a URL: {}", url);
            
            Usuario[] usuariosArray = restTemplate.getForObject(url, Usuario[].class);
            
            if (usuariosArray != null) {
                logger.info("Se obtuvieron {} usuarios", usuariosArray.length);
                for (Usuario usuario : usuariosArray) {
                    logger.info("Usuario: RUT={}, Nombre={}", usuario.getRut(), usuario.getNombre());
                }
            }
            
            return usuariosArray != null ? Arrays.asList(usuariosArray) : new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error al obtener todos los usuarios: {}", e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}