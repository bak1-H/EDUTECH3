package com.edutech.sistema.service;

import com.edutech.sistema.model.Curso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@Service
public class CursoService {

    @Autowired
    private RestTemplate restTemplate;

    public Curso obtenerCursoPorId(Long id) {
        String url = "http://localhost/api/cursos/" + id;
        return restTemplate.getForObject(url, Curso.class);
    }

    public List<Curso> obtenerTodosLosCursos() {
        String url = "http://localhost/api/cursos";
        Curso[] cursosArray = restTemplate.getForObject(url, Curso[].class);
        return cursosArray != null ? Arrays.asList(cursosArray) : new ArrayList<>();
    }
}