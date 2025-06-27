//Se crea el servicio TipoUsuarioService para manejar la logica de negocio
//relacionada con los tipos de usuario.

// Este servicio se encarga de inicializar los tipos de usuario básicos
// al iniciar la aplicación, y proporciona métodos para consultar los tipos de usuario.

//No le agregue el guardar o el eliminar, para hacerlo mas sencillo, y no agregar mas al asunto
//ya que el enfoque de la prueba esta para las pruebas unitarias.


package com.duoc.Fullstack3.services;
import com.duoc.Fullstack3.models.TipoUsuario;
import com.duoc.Fullstack3.repository.TipoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Optional;

@Service
public class TipoUsuarioService {

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    @PostConstruct  //PostConstruct sirve para inicializar datos al iniciar la aplicación
                    // Este método sirve para inicializar los tipos de usuario básicos si no existen
                    // Se ejecuta una vez al iniciar la aplicación
                    // Esto asegura que siempre tengamos los tipos básicos disponibles
                    // Si ya existen, no hace nada.
    public void inicializarTipos() {
        if (tipoUsuarioRepository.count() == 0) {
            crearTiposBasicos();
        }
    }

    private void crearTiposBasicos() {
        // ESTUDIANTE
        TipoUsuario estudiante = new TipoUsuario();
        estudiante.setNombre("ESTUDIANTE");
        estudiante.setDescripcion("Estudiante del sistema educativo");
        estudiante.setPermisos("{\"acceso_basico\":true}");
        estudiante.setActivo(true);
        tipoUsuarioRepository.save(estudiante);

        // PROFESOR
        TipoUsuario profesor = new TipoUsuario();
        profesor.setNombre("PROFESOR");
        profesor.setDescripcion("Profesor del sistema educativo");
        profesor.setPermisos("{\"acceso_avanzado\":true,\"gestionar_clases\":true}");
        profesor.setActivo(true);
        tipoUsuarioRepository.save(profesor);

        // TRABAJADOR
        TipoUsuario trabajador = new TipoUsuario();
        trabajador.setNombre("TRABAJADOR");
        trabajador.setDescripcion("Personal administrativo");
        trabajador.setPermisos("{\"acceso_administrativo\":true}");
        trabajador.setActivo(true);
        tipoUsuarioRepository.save(trabajador);

        System.out.println("Tipos básicos inicializados: ESTUDIANTE, PROFESOR, TRABAJADOR");
    }

  //Metodos de consulta joven Jim
    public List<TipoUsuario> obtenerTodos() {
        return tipoUsuarioRepository.findAll();
    }

    public Optional<TipoUsuario> obtenerPorId(Long id) {
        return tipoUsuarioRepository.findById(id);
    }

    public boolean existeTipo(Long id) {
        return tipoUsuarioRepository.existsById(id);
    }
}