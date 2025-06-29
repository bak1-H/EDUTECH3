package com.edutech.sistema.service;

import com.edutech.sistema.model.Pago;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PagoService pagoService;

    private Pago pagoEjemplo;

    @BeforeEach
    void setUp() {
        pagoEjemplo = new Pago();
        pagoEjemplo.setId(1L);
        pagoEjemplo.setEstado(true);

    }


    // Este test verifica que el método obtenerPagoPorId
    //retorna un pago cuando se le pasa un ID válido.
    // También verifica que el pago retornado tiene los valores esperados.
    // El test utiliza Mockito para simular el comportamiento del RestTemplate.
    @Test
    void testObtenerPagoPorId_Exitoso() {
        // Arrange
        Long pagoId = 1L;
        when(restTemplate.getForObject(anyString(), eq(Pago.class)))
            .thenReturn(pagoEjemplo);

        // Act
        Pago resultado = pagoService.obtenerPagoPorId(pagoId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    // Este test verifica que el método obtenerPagoPorId
    // lanza una excepción HttpClientErrorException cuando se intenta obtener un pago
    // con un ID que no existe. Utiliza Mockito para simular el comportamiento del RestTemplate.
    @Test
    void testObtenerPagoPorId_NoEncontrado() {
        // Arrange
        Long pagoId = 999L;
        when(restTemplate.getForObject(anyString(), eq(Pago.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act & Assert
        assertThrows(HttpClientErrorException.class, () -> {
            pagoService.obtenerPagoPorId(pagoId);
        });
    }


    // Este test verifica que el método obtenerTodosLosPagos
    // retorna una lista de pagos cuando se llama correctamente.
    // También verifica que la lista contiene los pagos esperados.
    // Utiliza Mockito para simular el comportamiento del RestTemplate.

    @Test
    void testObtenerTodosLosPagos_Exitoso() {
        // Arrange
        Pago pago2 = new Pago();
        pago2.setId(2L);
        
        Pago[] pagosArray = {pagoEjemplo, pago2};
        when(restTemplate.getForObject(anyString(), eq(Pago[].class)))
            .thenReturn(pagosArray);

        // Act
        List<Pago> resultado = pagoService.obtenerTodosLosPagos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());

    }

    // Este test verifica que el método obtenerTodosLosPagos
    // retorna una lista vacía cuando no hay pagos disponibles.
    // Utiliza Mockito para simular el comportamiento del RestTemplate.

    @Test
    void testObtenerTodosLosPagos_ListaVacia() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(Pago[].class)))
            .thenReturn(new Pago[0]);

        // Act
        List<Pago> resultado = pagoService.obtenerTodosLosPagos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }


    // Este test verifica que el método obtenerTodosLosPagos
    // lanza una excepción cuando ocurre un error al intentar obtener los pagos.
    // Utiliza Mockito para simular el comportamiento del RestTemplate.
    @Test
    void testObtenerTodosLosPagos_Error() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(Pago[].class)))
            .thenThrow(new RuntimeException("Error de conexión"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            pagoService.obtenerTodosLosPagos();
        });
    }
}