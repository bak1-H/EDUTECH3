package com.edutech.curso.service;

import com.edutech.curso.model.Curso;
import com.edutech.curso.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

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

