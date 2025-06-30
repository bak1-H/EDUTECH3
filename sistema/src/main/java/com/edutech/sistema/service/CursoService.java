// filepath: c:\Users\maxim\OneDrive\Escritorio\EDUTECH3-main\sistema\src\main\java\com\edutech\sistema\service\CursoService.java
package com.edutech.sistema.service;

import com.edutech.sistema.model.Curso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@Service
public class CursoService {

    private static final Logger logger = LoggerFactory.getLogger(CursoService.class);

    @Autowired
    private RestTemplate restTemplate;

    public Curso obtenerCursoPorId(Long cursoId) {
        try {
            String url = "http://localhost:8084/api/curso/" + cursoId;
            logger.info("Llamando a URL: {}", url);
            
            ResponseEntity<Curso> response = restTemplate.getForEntity(url, Curso.class);
            
            logger.info("Status Code: {}", response.getStatusCode());
            logger.info("Headers: {}", response.getHeaders());
            
            Curso curso = response.getBody();
            
            if (curso != null) {
                logger.info("DATOS DEL CURSO OBTENIDO:");
                logger.info("ID: {}", curso.getId());
                logger.info("Nombre: '{}'", curso.getNombreCurso());
                logger.info("Descripcion: '{}'", curso.getDescripcionCurso());
                
                if (curso.getNombreCurso() == null) {
                    logger.error("PROBLEMA: El nombre del curso es NULL");
                }
                if (curso.getDescripcionCurso() == null) {
                    logger.error("PROBLEMA: La descripcion del curso es NULL");
                }
            } else {
                logger.error("Curso retornado es null para ID: {}", cursoId);
            }
            
            return curso;
        } catch (ResourceAccessException e) {
            logger.error("ERROR: Microservicio de cursos no disponible en puerto 8084");
            logger.error("Verifica que el microservicio curso este ejecutandose");
            return null;
        } catch (HttpClientErrorException e) {
            logger.error("Error HTTP al obtener curso con ID {}: {} - {}", 
                        cursoId, e.getStatusCode(), e.getResponseBodyAsString());
            return null;
        } catch (Exception e) {
            logger.error("Error inesperado al obtener curso con ID {}: {}", cursoId, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Curso> obtenerTodosLosCursos() {
        try {
            String url = "http://localhost:8084/api/curso";
            logger.info("Llamando a URL: {}", url);
            
            ResponseEntity<Curso[]> response = restTemplate.getForEntity(url, Curso[].class);
            Curso[] cursosArray = response.getBody();
            
            if (cursosArray != null) {
                logger.info("Se obtuvieron {} cursos", cursosArray.length);
                for (Curso curso : cursosArray) {
                    logger.info("Curso: ID={}, Nombre='{}', Descripcion='{}'", 
                              curso.getId(), curso.getNombreCurso(), curso.getDescripcionCurso());
                }
                return Arrays.asList(cursosArray);
            } else {
                logger.warn("Array de cursos es null");
                return new ArrayList<>();
            }
            
        } catch (ResourceAccessException e) {
            logger.error("ERROR: Microservicio de cursos no disponible en puerto 8084");
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error al obtener todos los cursos: {}", e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}