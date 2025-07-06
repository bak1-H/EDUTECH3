package com.edutech.curso.service;

import com.edutech.curso.model.Curso;
import com.edutech.curso.repository.CursoRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {
    
    @PostConstruct
    public void inicializarTipos() {
        if (cursoRepository.count() == 0) {
            inicializarCursos();
        }
    }
    public void inicializarCursos() {
        // Este método se puede usar para inicializar datos de ejemplo al iniciar la aplicación
        // Por ejemplo, crear cursos predefinidos si no existen
        if (cursoRepository.count() == 0) {
            Curso curso1 = new Curso();
            curso1.setNombreCurso("Curso de Programación Java");
            curso1.setDescripcionCurso("Curso básico de programación en Java");
            cursoRepository.save(curso1);

            Curso curso2 = new Curso();
            curso2.setNombreCurso("Curso de Bases de Datos");
            curso2.setDescripcionCurso("Curso introductorio a bases de datos SQL");
            cursoRepository.save(curso2);
        }
    }

    @Autowired
    private CursoRepository cursoRepository;

    public Optional<Curso> obtenerPorId(Long id) {
        return cursoRepository.findById(id);
    }

    public List<Curso> obtenerTodos() {
        return cursoRepository.findAll();
    }

    public Curso guardarCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    public void eliminarCurso(Long id) {
        cursoRepository.deleteById(id);
    }

    // ← Método corregido que devuelve Curso
    public Curso actualizarCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    public boolean existeCurso(Long id) {
        return cursoRepository.existsById(id);
    }
}

