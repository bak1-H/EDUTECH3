package com.edutech.sistema.service;

import com.edutech.sistema.model.Pago;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CursoService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Pago obtenerPagoPorId(Long id) {
        String url = "http://localhost/api/cursos/" + id;
        return restTemplate.getForObject(url, Pago.class);
    }
}