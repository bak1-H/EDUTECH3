
package com.edutech.sistema.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.HashMap;


//SistemaService es un servicio que se encarga de interactuar con los microservicios externos
// para verificar la existencia de cursos y crear pagos enriquecidos con datos reales.

@Service
public class SistemaService {
    
    private static final Logger logger = LoggerFactory.getLogger(SistemaService.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    public boolean verificarCursoExiste(Long cursoId) {
        try {
            String url = "http://localhost:8084/api/curso/" + cursoId;
            restTemplate.getForObject(url, Object.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Curso con ID {} no encontrado", cursoId);
            return false;
        } catch (Exception e) {
            logger.error("Error al verificar curso: {}", e.getMessage());
            return false;
        }
    }
    
    public Long crearPago(Long usuarioRut, Long cursoId, Boolean estadoPago) {
        try {
            String url = "http://localhost:8083/api/pagos";
            
            Map<String, Object> pagoRequest = new HashMap<>();
            pagoRequest.put("usuarioRut", usuarioRut);
            pagoRequest.put("cursoId", cursoId);
            pagoRequest.put("estado", estadoPago != null ? estadoPago : false);
            
            Map<String, Object> response = restTemplate.postForObject(url, pagoRequest, Map.class);
            
            if (response != null && response.containsKey("id")) {
                return Long.valueOf(response.get("id").toString());
            }
            
            logger.warn("No se pudo obtener ID del pago creado");
            return null;
            
        } catch (Exception e) {
            logger.error("Error al crear pago: {}", e.getMessage());
            return null;
        }
    }
}