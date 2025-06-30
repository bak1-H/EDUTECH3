package com.edutech.usuario.service;

import com.edutech.usuario.model.Usuario;
import com.edutech.usuario.model.UsuarioRequest;
import com.edutech.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private MicroservicioService microservicioService;

    public Usuario crearUsuario(UsuarioRequest request) {
        try {
            // Validar que el curso existe si se proporciona
            if (request.getCursoId() != null) {
                if (!microservicioService.verificarCursoExiste(request.getCursoId())) {
                    throw new RuntimeException("El curso con ID " + request.getCursoId() + " no existe");
                }
            }
            
            // Validar email único
            if (request.getEmail() != null && usuarioRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Ya existe un usuario con ese email");
            }
            
            // Crear usuario
            Usuario usuario = new Usuario();
            usuario.setRut(request.getRut());
            usuario.setDv(request.getDv());
            usuario.setNombre(request.getNombre());
            usuario.setEmail(request.getEmail());
            usuario.setContrasena(request.getContrasena());
            usuario.setFechaRegistro(LocalDateTime.now());
            usuario.setTipoUsuarioId(request.getTipoUsuarioId());
            
            // Guardar usuario PRIMERO
            // Esto es necesario para que el microservicio pueda crear el pago enriquecido
            // con el RUT y nombre del usuario recién creado.
            // Si el usuario ya existe, se actualizará.
            // Si no, se creará un nuevo usuario.
            // Guardar el usuario en la base de datos
            // y registrar el evento de creación.
            // Esto asegura que el usuario tenga un RUT único y no se repita.
            // También se registra el evento de creación del usuario.
            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            logger.info("Usuario creado: {} (RUT: {})", usuarioGuardado.getNombre(), usuarioGuardado.getRut());
            
            // Crear pago enriquecido si se asignó un curso
            if (request.getCursoId() != null) {
                try {
                    boolean estadoPago = request.getEstadoPago() != null ? request.getEstadoPago() : false;
                    
                    Long pagoId = microservicioService.crearPagoEnriquecido(
                        usuarioGuardado.getRut(), 
                        usuarioGuardado.getNombre(),
                        request.getCursoId(), 
                        estadoPago
                    );
                    
                    if (pagoId != null) {
                        String estado = estadoPago ? "PAGADO" : "PENDIENTE";
                        logger.info("Pago creado - ID: {}, Usuario: {} ({}), Estado: {}", 
                                  pagoId, usuarioGuardado.getRut(), usuarioGuardado.getNombre(), estado);
                    }
                } catch (Exception e) {
                    logger.error("Error al crear pago para usuario {}: {}", usuarioGuardado.getRut(), e.getMessage());
                }
            }
            
            return usuarioGuardado;
        } catch (Exception e) {
            logger.error("Error al crear usuario: {}", e.getMessage());
            throw e;
        }
    }


    //Meotdo para obtener todos los usuarios
    // Este método obtiene todos los usuarios de la base de datos y maneja excepciones
    // También registra la cantidad de usuarios obtenidos
    // Si ocurre un error, se registra el error y se lanza una excepción
    public List<Usuario> obtenerTodos() {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            logger.info("Obtenidos {} usuarios", usuarios.size());
            return usuarios;
        } catch (Exception e) {
            logger.error("Error al obtener todos los usuarios: {}", e.getMessage());
            throw e;
        }
    }


    // Método para obtener un usuario por su RUT
    // Este método busca un usuario por su RUT, maneja excepciones y registra el
    // resultado. Si el RUT es inválido, lanza una excepción.
    // Si el usuario no se encuentra, devuelve null.
    // Si se encuentra, devuelve el usuario encontrado.
    // Si ocurre un error, se registra el error y se lanza una excepción.
    public Usuario obtenerPorRut(String rut) {
        try {
            Long rutLong = Long.parseLong(rut);
            Optional<Usuario> usuario = usuarioRepository.findById(rutLong);
            if (usuario.isPresent()) {
                logger.info("Usuario encontrado: {} (RUT: {})", usuario.get().getNombre(), rut);
                return usuario.get();
            } else {
                logger.warn("Usuario no encontrado con RUT: {}", rut);
                return null;
            }
        } catch (NumberFormatException e) {
            logger.error("RUT inválido: {}", rut);
            throw new RuntimeException("RUT inválido");
        } catch (Exception e) {
            logger.error("Error al obtener usuario por RUT {}: {}", rut, e.getMessage());
            throw e;
        }
    }


    // Método para actualizar un usuario por su RUT
    // Este método actualiza un usuario existente por su RUT, maneja excepciones y
    // registra el resultado. Si el RUT es inválido, lanza una excepción.
    // Si el usuario no se encuentra, devuelve null.
    // Si se encuentra, actualiza los campos proporcionados en el request y guarda el usuario
    // actualizado. Si ocurre un error, se registra el error y se lanza una excepción
    public Usuario actualizarUsuario(String rut, UsuarioRequest request) {
        try {
            Long rutLong = Long.parseLong(rut);
            Optional<Usuario> usuarioExistente = usuarioRepository.findById(rutLong);
            
            if (usuarioExistente.isPresent()) {
                Usuario usuario = usuarioExistente.get();
                
                // Validar email único si se está cambiando
                if (request.getEmail() != null && !request.getEmail().equals(usuario.getEmail())) {
                    if (usuarioRepository.existsByEmail(request.getEmail())) {
                        throw new RuntimeException("Ya existe un usuario con ese email");
                    }
                }
                
                usuario.setDv(request.getDv());
                usuario.setNombre(request.getNombre());
                usuario.setEmail(request.getEmail());
                usuario.setContrasena(request.getContrasena());
                usuario.setTipoUsuarioId(request.getTipoUsuarioId());
                
                Usuario usuarioActualizado = usuarioRepository.save(usuario);
                logger.info("Usuario actualizado: {} (RUT: {})", usuarioActualizado.getNombre(), rut);
                return usuarioActualizado;
            }
            return null;
        } catch (NumberFormatException e) {
            logger.error("RUT inválido: {}", rut);
            throw new RuntimeException("RUT inválido");
        } catch (Exception e) {
            logger.error("Error al actualizar usuario {}: {}", rut, e.getMessage());
            throw e;
        }
    }

    // Método para eliminar un usuario por su RUT
    // Este método elimina un usuario existente por su RUT, maneja excepciones y
    // registra el resultado. Si el RUT es inválido, lanza una excepción.
    // Si el usuario no se encuentra, devuelve false.
    // Si se encuentra, elimina el usuario y devuelve true.
    // Si ocurre un error, se registra el error y se lanza una excepción.
    public boolean eliminarUsuario(String rut) {
        try {
            Long rutLong = Long.parseLong(rut);
            if (usuarioRepository.existsById(rutLong)) {
                usuarioRepository.deleteById(rutLong);
                logger.info("Usuario eliminado con RUT: {}", rut);
                return true;
            }
            logger.warn("Usuario no encontrado para eliminar con RUT: {}", rut);
            return false;
        } catch (NumberFormatException e) {
            logger.error("RUT inválido: {}", rut);
            throw new RuntimeException("RUT inválido");
        } catch (Exception e) {
            logger.error("Error al eliminar usuario {}: {}", rut, e.getMessage());
            throw e;
        }
    }

    // Método para guardar un usuario
    // Este método guarda un usuario en la base de datos, maneja excepciones y
    // registra el resultado. Si la fecha de registro es nula, se establece la fecha
    // actual como fecha de registro.
    // Si ocurre un error, se registra el error y se lanza una excepción.
    // Este método es útil para crear o actualizar usuarios.
    // Si el usuario ya existe, se actualiza; si no, se crea uno nuevo
    // y se guarda en la base de datos.
    public Usuario guardarUsuario(Usuario usuario) {
        try {
            if (usuario.getFechaRegistro() == null) {
                usuario.setFechaRegistro(LocalDateTime.now());
            }
            return usuarioRepository.save(usuario);
        } catch (Exception e) {
            logger.error("Error al guardar usuario: {}", e.getMessage());
            throw e;
        }
    }

    // Métodos adicionales útiles
    public List<Usuario> obtenerPorTipoUsuario(Long tipoUsuarioId) {
        return usuarioRepository.findByTipoUsuarioId(tipoUsuarioId);
    }

    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }
}