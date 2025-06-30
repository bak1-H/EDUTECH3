package com.edutech.usuario.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;


//Este servicio se encarga de interactuar con los microservicios externos
// para verificar la existencia de cursos y crear pagos enriquecidos con datos reales.
@Service
public class MicroservicioService {
    
    private static final Logger logger = LoggerFactory.getLogger(MicroservicioService.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    public boolean verificarCursoExiste(Long cursoId) {
        try {
            String url = "http://localhost:8084/api/curso/" + cursoId;
            restTemplate.getForObject(url, Object.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (Exception e) {
            logger.error("Error al verificar curso: {}", e.getMessage());
            return false;
        }
    }
    

    // Método para obtener el nombre del curso
    // Si el nombre es null, intenta obtenerlo desde otro campo

    public String obtenerNombreCurso(Long cursoId) {
        try {
            String url = "http://localhost:8084/api/curso/" + cursoId;
            Map<String, Object> cursoData = restTemplate.getForObject(url, Map.class);
            
            if (cursoData != null) {
                String nombre = (String) cursoData.get("nombreCurso");
                if (nombre == null) {
                    nombre = (String) cursoData.get("nombre");
                }
                return nombre != null ? nombre : "Curso sin nombre";
            }
            return "Curso sin nombre";
        } catch (Exception e) {
            logger.error("Error al obtener nombre del curso {}: {}", cursoId, e.getMessage());
            return "Curso sin nombre";
        }
    }
    
    // MÉTODO PRINCIPAL - usar siempre el nombre real del usuario
    public Long crearPagoEnriquecido(Long usuarioRut, String nombreUsuario, Long cursoId, boolean estadoPago) {
        try {
            // Obtener nombre del curso
            String nombreCurso = obtenerNombreCurso(cursoId);
            
            // Crear pago con datos reales
            Map<String, Object> pagoData = Map.of(
                "usuarioRut", usuarioRut,
                "nombreUsuario", nombreUsuario, // USAR NOMBRE REAL SIEMPRE
                "cursoId", cursoId,
                "nombreCurso", nombreCurso,
                "estado", estadoPago
            );
            
            logger.info("Creando pago enriquecido: Usuario={} ({}), Curso={} ({}), Estado={}", 
                       usuarioRut, nombreUsuario, cursoId, nombreCurso, estadoPago ? "PAGADO" : "PENDIENTE");
            
            // Enviar al microservicio pago
            String url = "http://localhost:8083/api/pagos";
            Map<String, Object> response = restTemplate.postForObject(url, pagoData, Map.class);
            
            if (response != null && response.get("id") != null) {
                Long pagoId = Long.valueOf(response.get("id").toString());
                logger.info("Pago enriquecido creado exitosamente con ID: {} - Usuario: {}", pagoId, nombreUsuario);
                return pagoId;
            }
            
            return null;
        } catch (Exception e) {
            logger.error("Error al crear pago enriquecido: {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
}