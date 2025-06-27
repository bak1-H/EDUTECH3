//Se crea el servicio UsuarioService que maneja la lógica de negocio relacionada con los usuarios
// Este servicio interactúa con el repositorio de usuarios y el cliente de tipo usuario para enriquecer
// la información de los usuarios con sus tipos correspondientes.

//Enriquecer significa agregar información adicional a los objetos Usuario
// obtenidos del repositorio, específicamente el tipo de usuario asociado a cada usuario
// Esto permite que al obtener un usuario, también se obtenga su tipo de usuario
// sin necesidad de hacer una consulta adicional al microservicio de tipo usuario.

//En conclusion JIM es para que se vea mas bonito y no salga un "null" en el tipo de usuario.


package com.edutech.usuario.service;
import com.edutech.usuario.model.*;
import com.edutech.usuario.repository.repository_usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private repository_usuario usuarioRepository;

    @Autowired
    private TipoUsuarioClient tipoUsuarioClient;

    public List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        usuarios.forEach(this::enriquecerConTipoUsuario);
        return usuarios;
    }

    public Optional<Usuario> obtenerPorRut(Long rut) {
        Optional<Usuario> usuario = usuarioRepository.findById(rut);
        if (usuario.isPresent()) {
            enriquecerConTipoUsuario(usuario.get());
        }
        return usuario;
    }

    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario.getFechaRegistro() == null) {
            usuario.setFechaRegistro(new Timestamp(System.currentTimeMillis()));
        }
        
        Usuario resultado = usuarioRepository.save(usuario);
        enriquecerConTipoUsuario(resultado);
        return resultado;
    }

    public void eliminarUsuario(Long rut) {
        usuarioRepository.deleteById(rut);
    }

    private void enriquecerConTipoUsuario(Usuario usuario) {
        try {
            TipoUsuarioDto tipoUsuario = tipoUsuarioClient.obtenerTipoUsuario(usuario.getTipoUsuarioId());
            usuario.setTipoUsuario(tipoUsuario);
        } catch (Exception e) {
            usuario.setTipoUsuario(new TipoUsuarioDto(
                usuario.getTipoUsuarioId(), 
                "ERROR", 
                "No disponible",
                "{}",
                false
            ));
        }
    }
}