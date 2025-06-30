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

//CursoService es un servicio que se encarga de interactuar con el microservicio de cursos
// para obtener información sobre cursos. Este servicio utiliza RestTemplate para realizar
// solicitudes HTTP al microservicio de cursos y manejar las respuestas. Proporciona métodos
// para obtener un curso por su ID y para obtener todos los cursos disponibles. Además,
// maneja excepciones específicas como ResourceAccessException y HttpClientErrorException
// para registrar errores y retornar respuestas adecuadas en caso de problemas de conexión
// o errores HTTP. Utiliza SLF4J para registrar información relevante durante la ejecución
// de los métodos, incluyendo detalles del curso obtenido y posibles problemas con los datos.


@Service
public class CursoService {

    private static final Logger logger = LoggerFactory.getLogger(CursoService.class);

    @Autowired
    private RestTemplate restTemplate;

    public Curso obtenerCursoPorId(Long cursoId) {
        try {
            String url = "http://localhost:8084/api/curso/" + cursoId;
            logger.info("Llamando a URL: {}", url);
            
            // Realiza la solicitud GET al microservicio de cursos
            // y espera una respuesta de tipo Curso
            // ResponseEntity es una clase que representa una respuesta HTTP completa
            // incluyendo el cuerpo, los encabezados y el código de estado.
            ResponseEntity<Curso> response = restTemplate.getForEntity(url, Curso.class);
            
            logger.info("Status Code: {}", response.getStatusCode());
            logger.info("Headers: {}", response.getHeaders());
            
            // Obtiene el cuerpo de la respuesta, que debe ser un objeto Curso
            // El cuerpo es el contenido de la respuesta HTTP, que en este caso es un objeto
            // Curso que contiene información sobre el curso solicitado.
            // Si la respuesta es exitosa, el cuerpo contendrá los datos del curso.
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
        } 

        // Captura excepciones específicas para manejar errores de conexión y errores HTTP
        // ResourceAccessException se lanza cuando hay un problema de conexión al intentar acceder al recurso
        // HttpClientErrorException se lanza cuando la respuesta del servidor es un error HTTP (4xx)
        // y contiene información sobre el error, como el código de estado y el cuerpo
        // de la respuesta.
        catch (ResourceAccessException e) {
            logger.error("ERROR: Microservicio de cursos no disponible en puerto 8084");
            logger.error("Verifica que el microservicio curso este ejecutandose");
            return null;
        } 
        
        // HttpClientErrorException se lanza cuando el servidor responde con un error HTTP
        // (por ejemplo, 404 Not Found o 500 Internal Server Error).
        // En este caso, se captura la excepción y se registra el error con el ID del curso
        // y el código de estado HTTP. También se registra el cuerpo de la respuesta para
        // proporcionar más detalles sobre el error.
        
        catch (HttpClientErrorException e) {
            logger.error("Error HTTP al obtener curso con ID {}: {} - {}", 
                        cursoId, e.getStatusCode(), e.getResponseBodyAsString());
            return null;
        } 
        
       // Captura cualquier otra excepción inesperada que pueda ocurrir durante la ejecución
        // del método. Esto es útil para manejar errores que no se anticiparon específicamente
        // y para registrar información adicional sobre el error.
        // Se registra el mensaje de error y se imprime la traza de la pila para ayudar
        // a identificar el problema en el código. 

        catch (Exception e) {
            logger.error("Error inesperado al obtener curso con ID {}: {}", cursoId, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    // Método para obtener todos los cursos disponibles
    // Este método realiza una solicitud GET al microservicio de cursos
    // y espera una respuesta que contenga un array de objetos Curso.
    // Si la respuesta es exitosa, convierte el array en una lista de objetos Curso
    // y los retorna. Si ocurre un error de conexión o un error HTTP, maneja
    // las excepciones adecuadamente, registrando el error y retornando una lista vacía
    // para evitar que la aplicación falle.
    public List<Curso> obtenerTodosLosCursos() {

        // Intenta realizar la solicitud al microservicio de cursos
        // y manejar la respuesta. Si ocurre un error, se captura y maneja adecuadamente
        // para evitar que la aplicación falle y proporcionar una respuesta adecuada.
        try {
            String url = "http://localhost:8084/api/curso";
            logger.info("Llamando a URL: {}", url);
            
            // Realiza la solicitud GET al microservicio de cursos
            // y espera una respuesta de tipo Curso[]
            // ResponseEntity es una clase que representa una respuesta HTTP completa
            // incluyendo el cuerpo, los encabezados y el código de estado.
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