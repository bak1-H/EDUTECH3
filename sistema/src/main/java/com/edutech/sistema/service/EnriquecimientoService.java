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


//EnrequicimientoService es un servicio que se encarga de enriquecer los datos de pagos, cursos y usuarios
// con información adicional proveniente de otros servicios. Utiliza los servicios UsuarioService, CursoService


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

        //Se crea un objeto PagoEnriquecido para almacenar los datos enriquecidos
        // Se copia la información básica del pago original

        PagoEnriquecido pagoEnriquecido = new PagoEnriquecido();
        pagoEnriquecido.setId(pago.getId());
        pagoEnriquecido.setEstado(pago.isEstado());
        pagoEnriquecido.setUsuarioRut(pago.getUsuarioRut());
        pagoEnriquecido.setCursoId(pago.getCursoId());

        // acá se intenta enriquecer el pago con información adicional de usuario y curso
        // Enriquecer con datos de usuario
        // Se intenta obtener el usuario por su RUT y se maneja cualquier excepción que pueda ocurrir
        // Si el usuario no se encuentra, se registra un mensaje de advertencia y se establece el usuario como null.
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

        // Acá se intenta enriquecer el pago con información adicional del curso
        // Se intenta obtener el curso por su ID y se maneja cualquier excepción que pueda ocurrir
        // Si el curso no se encuentra, se registra un mensaje de advertencia y se establece el curso como null.
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


        //AL final si se logra enriquecer el pago, se retorna el objeto PagoEnriquecido
        // Si no se logra enriquecer, se retorna un PagoEnriquecido con los datos básicos del pago original
        return pagoEnriquecido;
    }


    // Este método recibe una lista de pagos y devuelve una lista de pagos enriquecidos
    // Utiliza el método enriquecerPago para enriquecer cada pago de la lista
    public List<PagoEnriquecido> enriquecerPagos(List<Pago> pagos) {
        return pagos.stream()
                .map(this::enriquecerPago)
                .collect(Collectors.toList());
    }


    // Este método recibe un curso y lo enriquece con información adicional
    // como los pagos relacionados y los usuarios inscritos en el curso.
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

                    //.map sirve para transformar cada PagoEnriquecido en su Usuario asociado
                    // Se obtiene el usuario de cada pago enriquecido
                    .map(PagoEnriquecido::getUsuario)
                    // .filter se utiliza para filtrar los usuarios que no son nulos
                    .filter(usuario -> usuario != null)
                    // .distinct se utiliza para eliminar duplicados, asegurando que cada usuario sea único
                    .distinct()
                    // .collect se utiliza para recolectar los usuarios únicos en una lista
                    .collect(Collectors.toList());

            // Establecer los usuarios inscritos en el curso enriquecido
            // Se asigna la lista de usuarios inscritos al curso enriquecido
            cursoEnriquecido.setUsuariosInscritos(usuariosInscritos);

            // Establecer el total de inscritos en el curso enriquecido
            // Se cuenta el número de usuarios inscritos y se asigna al campo totalInscritos
            cursoEnriquecido.setTotalInscritos(usuariosInscritos.size());

        } catch (Exception e) {
            logger.error("Error al enriquecer curso con ID {}: {}", curso.getId(), e.getMessage());
            cursoEnriquecido.setUsuariosInscritos(new ArrayList<>());
            cursoEnriquecido.setTotalInscritos(0);
            cursoEnriquecido.setPagosRelacionados(new ArrayList<>());
        }

        return cursoEnriquecido;
    }


    // Este método recibe una lista de cursos y devuelve una lista de cursos enriquecidos
    // Utiliza el método enriquecerCurso para enriquecer cada curso de la lista
    // Se utiliza el método stream() para procesar la lista de cursos de manera funcional
    public List<CursoEnriquecido> enriquecerCursos(List<Curso> cursos) {
        return cursos.stream()
                // .map se utiliza para aplicar el método enriquecerCurso a cada curso de la lista
                .map(this::enriquecerCurso)
                // .collect se utiliza para recolectar los resultados en una lista
                .collect(Collectors.toList());
    }


    // Este método recibe un ID de curso y lo enriquece con información adicional
    // Utiliza el método enriquecerCurso para enriquecer el curso obtenido por su
    public CursoEnriquecido enriquecerCursoPorId(Long cursoId) {
      
        // Se intenta obtener el curso por su ID y se maneja cualquier excepción que pueda ocurrir
        // Si el curso no se encuentra, se registra un mensaje de error y se retorna null
        try {
            Curso curso = cursoService.obtenerCursoPorId(cursoId);

            // Si el curso es encontrado, se enriquece con información adicional
            // como los pagos relacionados y los usuarios inscritos en el curso.
            return curso != null ? enriquecerCurso(curso) : null;
        } catch (Exception e) {
            logger.error("Error al enriquecer curso por ID {}: {}", cursoId, e.getMessage());
            return null;
        }
    }


    //Metodo para enriquecer un usuario con información adicional
    // Este método recibe un objeto Usuario y lo enriquece con información adicional
    // en este caso con los cursos agregados por el usuario.
    // Utiliza los servicios UsuarioService, CursoService y PagoService para obtener la información necesaria
    public UsuarioEnriquecido enriquecerUsuario(Usuario usuario) {

        //acá se crea un objeto UsuarioEnriquecido para almacenar los datos enriquecidos
        // Se copia la información básica del usuario original
        UsuarioEnriquecido usuarioEnriquecido = new UsuarioEnriquecido();
        usuarioEnriquecido.setRut(usuario.getRut());
        usuarioEnriquecido.setNombre(usuario.getNombre());
        usuarioEnriquecido.setEmail(usuario.getEmail());

        try {
            logger.info("Enriqueciendo usuario: {}", usuario.getRut());
            
            // acá se obtienen todos los pagos del servicio PagoService
            // Se obtiene una lista de todos los pagos registrados en el sistema
            List<Pago> todosPagos = pagoService.obtenerTodosLosPagos();
            logger.info("Total de pagos obtenidos: {}", todosPagos.size());
            
            //esta linea filtra los pagos del usuario actual
            // Se filtran los pagos para obtener solo aquellos que pertenecen al usuario actual
            Long usuarioRutLong = Long.parseLong(usuario.getRut());

            // Se utiliza stream para filtrar los pagos del usuario por su RUT
            // y se recolectan en una lista
            List<Pago> pagosDelUsuario = todosPagos.stream()

                    // .filter se utiliza para filtrar los pagos que coinciden con el RUT del usuario
                    // Se compara el RUT del pago con el RUT del usuario convertido a Long
                    .filter(pago -> pago.getUsuarioRut().equals(usuarioRutLong))

                    // .map se utiliza para transformar cada Pago en su ID de curso
                    // Se obtiene el ID del curso asociado a cada pago
                    .collect(Collectors.toList());
            
            //logger.info sirve para registrar información en la consola
            // y es útil para depurar y monitorear el comportamiento de la aplicación
            logger.info("Pagos encontrados para usuario {}: {}", usuario.getRut(), pagosDelUsuario.size());

            // se crea una lista para almacenar los cursos agregados por el usuario
            // Esta lista contendrá objetos Curso simplificados
            List<Curso> cursosAgregados = new ArrayList<>();
            


            // Se itera sobre cada pago del usuario para obtener los cursos asociados
            // Se utiliza un bucle for para recorrer cada pago del usuario
            for (Pago pago : pagosDelUsuario) {
                try {
                    logger.info("Obteniendo curso con ID: {}", pago.getCursoId());
                    Curso curso = cursoService.obtenerCursoPorId(pago.getCursoId());
                    if (curso != null) {
                        logger.info("Curso obtenido: {} - {}", curso.getNombreCurso(), curso.getDescripcionCurso());
                        
                        // Crear CursoSimplificado
                        Curso cursoSimplificado = new Curso();
                        cursoSimplificado.setId(curso.getId());
                        cursoSimplificado.setNombreCurso(curso.getNombreCurso());
                        cursoSimplificado.setDescripcionCurso(curso.getDescripcionCurso());
                        
                        //acá se agrega el curso simplificado a la lista de cursos agregados
                        // Se agrega el curso simplificado a la lista de cursos agregados del usuario
                        cursosAgregados.add(cursoSimplificado);

                    } else {
                        // Si el curso es null, se registra un mensaje de advertencia
                        // Esto puede ocurrir si el curso no existe o no se pudo obtener
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


    // Este método recibe una lista de usuarios y devuelve una lista de usuarios enriquecidos
    // Utiliza el método enriquecerUsuario para enriquecer cada usuario de la lista
    public List<UsuarioEnriquecido> enriquecerUsuarios(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(this::enriquecerUsuario)
                .collect(Collectors.toList());
    }


    // Este método recibe un RUT de usuario y lo enriquece con información adicional
    // Utiliza el método enriquecerUsuario para enriquecer el usuario obtenido por su RUT
    // Se utiliza el servicio UsuarioService para obtener el usuario por su RUT
    public UsuarioEnriquecido enriquecerUsuarioPorRut(String rut) {
        
        // Se intenta obtener el usuario por su RUT y se maneja cualquier excepción que pueda ocurrir
        // Si el usuario no se encuentra, se registra un mensaje de error y se retorna null
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorRut(rut);
            return usuario != null ? enriquecerUsuario(usuario) : null;
        } catch (Exception e) {
            logger.error("Error al enriquecer usuario por RUT {}: {}", rut, e.getMessage());
            return null;
        }
    }
}