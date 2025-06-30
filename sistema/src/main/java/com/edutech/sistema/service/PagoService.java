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

@Service
public class PagoService {

    private static final Logger logger = LoggerFactory.getLogger(PagoService.class);

    @Autowired
    private RestTemplate restTemplate;

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