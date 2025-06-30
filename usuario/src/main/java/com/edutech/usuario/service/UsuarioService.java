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