package com.edutech.sistema.service;

import com.edutech.sistema.model.enriquecimiento.PagoEnriquecido;
import com.edutech.sistema.model.enriquecimiento.CursoEnriquecido;
import com.edutech.sistema.model.enriquecimiento.UsuarioEnriquecido;
import com.edutech.sistema.model.Curso;
import com.edutech.sistema.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class EnriquecimientoService {

    private static final Logger logger = LoggerFactory.getLogger(EnriquecimientoService.class);

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private PagoService pagoService;

    public PagoEnriquecido enriquecerPago(Pago pago) {
        PagoEnriquecido pagoEnriquecido = new PagoEnriquecido();
        pagoEnriquecido.setId(pago.getId());
        pagoEnriquecido.setEstado(pago.isEstado());
        pagoEnriquecido.setUsuarioRut(pago.getUsuarioRut());
        pagoEnriquecido.setCursoId(pago.getCursoId());

        // Enriquecer con datos de usuario
        try {
            logger.info("Intentando obtener usuario con RUT: {}", pago.getUsuarioRut());
            Usuario usuario = usuarioService.obtenerUsuarioPorRut(String.valueOf(pago.getUsuarioRut()));
            if (usuario != null) {
                logger.info("Usuario obtenido: {}", usuario.getNombre());
                pagoEnriquecido.setUsuario(usuario);
            } else {
                logger.warn("Usuario es null para RUT: {}", pago.getUsuarioRut());
                pagoEnriquecido.setUsuario(null);
            }
        } catch (Exception e) {
            logger.error("Error al obtener usuario con RUT {}: {}", pago.getUsuarioRut(), e.getMessage());
            pagoEnriquecido.setUsuario(null);
        }

        // Enriquecer con datos de curso
        try {
            logger.info("Intentando obtener curso con ID: {}", pago.getCursoId());
            Curso curso = cursoService.obtenerCursoPorId(pago.getCursoId());
            if (curso != null) {
                logger.info("Curso obtenido: {} - {}", curso.getNombreCurso(), curso.getDescripcionCurso());
                pagoEnriquecido.setCurso(curso);
            } else {
                logger.warn("Curso es null para ID: {}", pago.getCursoId());
                pagoEnriquecido.setCurso(null);
            }
        } catch (Exception e) {
            logger.error("Error al obtener curso con ID {}: {}", pago.getCursoId(), e.getMessage());
            pagoEnriquecido.setCurso(null);
        }

        return pagoEnriquecido;
    }

    public List<PagoEnriquecido> enriquecerPagos(List<Pago> pagos) {
        return pagos.stream()
                .map(this::enriquecerPago)
                .collect(Collectors.toList());
    }

    public CursoEnriquecido enriquecerCurso(Curso curso) {
        CursoEnriquecido cursoEnriquecido = new CursoEnriquecido();
        cursoEnriquecido.setId(curso.getId());
        cursoEnriquecido.setNombre(curso.getNombreCurso());
        cursoEnriquecido.setDescripcion(curso.getDescripcionCurso());
        // No incluir precio en CursoEnriquecido

        try {
            // Obtener todos los pagos
            List<Pago> todosPagos = pagoService.obtenerTodosLosPagos();
            
            // Filtrar pagos por curso ID
            List<Pago> pagosDelCurso = todosPagos.stream()
                    .filter(pago -> pago.getCursoId().equals(curso.getId()))
                    .collect(Collectors.toList());

            // Enriquecer los pagos del curso
            List<PagoEnriquecido> pagosEnriquecidos = enriquecerPagos(pagosDelCurso);
            cursoEnriquecido.setPagosRelacionados(pagosEnriquecidos);

            // Extraer usuarios únicos de los pagos
            List<Usuario> usuariosInscritos = pagosEnriquecidos.stream()
                    .map(PagoEnriquecido::getUsuario)
                    .filter(usuario -> usuario != null)
                    .distinct()
                    .collect(Collectors.toList());

            cursoEnriquecido.setUsuariosInscritos(usuariosInscritos);
            cursoEnriquecido.setTotalInscritos(usuariosInscritos.size());

        } catch (Exception e) {
            logger.error("Error al enriquecer curso con ID {}: {}", curso.getId(), e.getMessage());
            cursoEnriquecido.setUsuariosInscritos(new ArrayList<>());
            cursoEnriquecido.setTotalInscritos(0);
            cursoEnriquecido.setPagosRelacionados(new ArrayList<>());
        }

        return cursoEnriquecido;
    }

    public List<CursoEnriquecido> enriquecerCursos(List<Curso> cursos) {
        return cursos.stream()
                .map(this::enriquecerCurso)
                .collect(Collectors.toList());
    }

    public CursoEnriquecido enriquecerCursoPorId(Long cursoId) {
        try {
            Curso curso = cursoService.obtenerCursoPorId(cursoId);
            return curso != null ? enriquecerCurso(curso) : null;
        } catch (Exception e) {
            logger.error("Error al enriquecer curso por ID {}: {}", cursoId, e.getMessage());
            return null;
        }
    }

    // MÉTODO CORREGIDO PARA ENRIQUECER USUARIOS CON CursoSimplificado
    public UsuarioEnriquecido enriquecerUsuario(Usuario usuario) {
        UsuarioEnriquecido usuarioEnriquecido = new UsuarioEnriquecido();
        usuarioEnriquecido.setRut(usuario.getRut());
        usuarioEnriquecido.setNombre(usuario.getNombre());
        usuarioEnriquecido.setEmail(usuario.getEmail());

        try {
            logger.info("Enriqueciendo usuario: {}", usuario.getRut());
            
            // Obtener todos los pagos
            List<Pago> todosPagos = pagoService.obtenerTodosLosPagos();
            logger.info("Total de pagos obtenidos: {}", todosPagos.size());
            
            // Filtrar pagos por usuario RUT
            Long usuarioRutLong = Long.parseLong(usuario.getRut());
            List<Pago> pagosDelUsuario = todosPagos.stream()
                    .filter(pago -> pago.getUsuarioRut().equals(usuarioRutLong))
                    .collect(Collectors.toList());
            
            logger.info("Pagos encontrados para usuario {}: {}", usuario.getRut(), pagosDelUsuario.size());

            // Crear lista de cursos simplificados (sin precio)
            List<Curso> cursosAgregados = new ArrayList<>();
            
            for (Pago pago : pagosDelUsuario) {
                try {
                    logger.info("Obteniendo curso con ID: {}", pago.getCursoId());
                    Curso curso = cursoService.obtenerCursoPorId(pago.getCursoId());
                    if (curso != null) {
                        logger.info("Curso obtenido: {} - {}", curso.getNombreCurso(), curso.getDescripcionCurso());
                        
                        // Crear CursoSimplificado sin precio
                        Curso cursoSimplificado = new Curso();
                        cursoSimplificado.setId(curso.getId());
                        cursoSimplificado.setNombreCurso(curso.getNombreCurso());
                        cursoSimplificado.setDescripcionCurso(curso.getDescripcionCurso());
                        // No incluir precio
                        
                        cursosAgregados.add(cursoSimplificado);
                    } else {
                        logger.warn("Curso con ID {} es null", pago.getCursoId());
                    }
                } catch (Exception e) {
                    logger.error("Error al obtener curso con ID {} para usuario {}: {}", 
                              pago.getCursoId(), usuario.getRut(), e.getMessage());
                }
            }

            usuarioEnriquecido.setCursosAgregados(cursosAgregados);
            logger.info("Usuario {} enriquecido con {} cursos", usuario.getRut(), cursosAgregados.size());

        } catch (Exception e) {
            logger.error("Error al enriquecer usuario con RUT {}: {}", usuario.getRut(), e.getMessage());
            usuarioEnriquecido.setCursosAgregados(new ArrayList<>());
        }

        return usuarioEnriquecido;
    }

    public List<UsuarioEnriquecido> enriquecerUsuarios(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::enriquecerUsuario)
                .collect(Collectors.toList());
    }

    public UsuarioEnriquecido enriquecerUsuarioPorRut(String rut) {
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorRut(rut);
            return usuario != null ? enriquecerUsuario(usuario) : null;
        } catch (Exception e) {
            logger.error("Error al enriquecer usuario por RUT {}: {}", rut, e.getMessage());
            return null;
        }
    }
}