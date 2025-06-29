package com.edutech.sistema.service;

import com.edutech.sistema.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@Service
public class UsuarioService {

    @Autowired
    private RestTemplate restTemplate;

    public Usuario obtenerUsuarioPorRut(String rut) {
        String url = "http://localhost/api/usuarios/" + rut;
        return restTemplate.getForObject(url, Usuario.class);
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        String url = "http://localhost/api/usuarios";
        Usuario[] usuariosArray = restTemplate.getForObject(url, Usuario[].class);
        return usuariosArray != null ? Arrays.asList(usuariosArray) : new ArrayList<>();
    }
}