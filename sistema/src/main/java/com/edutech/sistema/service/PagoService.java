package com.edutech.sistema.service;

import com.edutech.sistema.model.Pago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;



// PagoService es un servicio que se encarga de interactuar con el microservicio de pagos
// para obtener información sobre pagos. Este servicio utiliza RestTemplate para realizar
// solicitudes HTTP al microservicio de pagos y manejar las respuestas. Proporciona métodos
// para obtener un pago por su ID y para obtener todos los pagos disponibles. Además,
// maneja excepciones específicas como HttpClientErrorException.NotFound para registrar
// advertencias y otros errores para registrar mensajes de error.
@Service
public class PagoService {

    private static final Logger logger = LoggerFactory.getLogger(PagoService.class);

    @Autowired
    private RestTemplate restTemplate;


    // Método para obtener un pago por su ID
    // Este método realiza una solicitud GET al microservicio de pagos
    // y retorna un objeto Pago si se encuentra, o null si no se encuentra o hay un error
    // Utiliza RestTemplate para realizar la solicitud HTTP y maneja excepciones específicas.
    public Pago obtenerPagoPorId(Long id) {
        try {
            String url = "http://localhost:8083/api/pagos/" + id;
            return restTemplate.getForObject(url, Pago.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Pago con ID {} no encontrado", id);
            return null;
        } catch (Exception e) {
            logger.error("Error al obtener pago con ID {}: {}", id, e.getMessage());
            return null;
        }
    }


    // Método para obtener todos los pagos
    // Este método realiza una solicitud GET al microservicio de pagos
    // y retorna una lista de objetos Pago. Si no se encuentran pagos o hay un error,
    // retorna una lista vacía. Utiliza RestTemplate para realizar la solicitud HTTP
    // y maneja excepciones específicas como HttpClientErrorException.NotFound para registrar
    // advertencias y otros errores para registrar mensajes de error.
    public List<Pago> obtenerTodosLosPagos() {
        try {
            String url = "http://localhost:8083/api/pagos";
            Pago[] pagosArray = restTemplate.getForObject(url, Pago[].class);
            return pagosArray != null ? Arrays.asList(pagosArray) : new ArrayList<>();
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Endpoint de pagos no encontrado");
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error al obtener todos los pagos: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
}