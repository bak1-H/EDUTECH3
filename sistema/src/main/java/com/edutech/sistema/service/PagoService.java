package com.edutech.sistema.service;

import com.edutech.sistema.model.Pago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@Service
public class PagoService {

    @Autowired
    private RestTemplate restTemplate;

    public Pago obtenerPagoPorId(Long id) {
        String url = "http://localhost/api/pagos/" + id;
        return restTemplate.getForObject(url, Pago.class);
    }

    public List<Pago> obtenerTodosLosPagos() {
        String url = "http://localhost/api/pagos";
        Pago[] pagosArray = restTemplate.getForObject(url, Pago[].class);
        return pagosArray != null ? Arrays.asList(pagosArray) : new ArrayList<>();
    }
}