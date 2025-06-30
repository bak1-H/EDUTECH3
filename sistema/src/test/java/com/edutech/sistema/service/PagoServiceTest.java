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
import org.springframework.web.client.ResourceAccessException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

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
        pagoEjemplo.setUsuarioRut(12345678L);
        pagoEjemplo.setCursoId(101L);
    }


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
        assertTrue(resultado.isEstado());
        assertEquals(12345678L, resultado.getUsuarioRut());
        assertEquals(101L, resultado.getCursoId());
        
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago.class));
    }

    @Test
    void testObtenerPagoPorId_NoEncontrado() {
        // Arrange
        Long pagoId = 999L;
        when(restTemplate.getForObject(anyString(), eq(Pago.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        
        // Act
        Pago resultado = pagoService.obtenerPagoPorId(pagoId);
        
        // Assert - El servicio captura la excepción y devuelve null
        assertNull(resultado, "El servicio debe retornar null cuando el pago no se encuentra");
        
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago.class));
    }

    @Test
    void testObtenerPagoPorId_ErrorConexion() {
        // Arrange
        Long pagoId = 1L;
        when(restTemplate.getForObject(anyString(), eq(Pago.class)))
            .thenThrow(new ResourceAccessException("Microservicio no disponible"));
        
        // Act
        Pago resultado = pagoService.obtenerPagoPorId(pagoId);
        
        // Assert - El servicio captura la excepción y devuelve null
        assertNull(resultado, "El servicio debe retornar null cuando hay error de conexión");
        
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago.class));
    }

    @Test
    void testObtenerPagoPorId_ErrorGenerico() {
        // Arrange
        Long pagoId = 1L;
        when(restTemplate.getForObject(anyString(), eq(Pago.class)))
            .thenThrow(new RuntimeException("Error inesperado"));
        
        // Act
        Pago resultado = pagoService.obtenerPagoPorId(pagoId);
        
        // Assert - El servicio captura todas las excepciones y devuelve null
        assertNull(resultado, "El servicio debe retornar null en caso de error genérico");
        
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago.class));
    }

    // === TESTS PARA obtenerTodosLosPagos ===

    @Test
    void testObtenerTodosLosPagos_Exitoso() {
        // Arrange
        Pago pago2 = new Pago();
        pago2.setId(2L);
        pago2.setEstado(false);
        pago2.setUsuarioRut(87654321L);
        pago2.setCursoId(102L);
        
        Pago[] pagosArray = {pagoEjemplo, pago2};
        when(restTemplate.getForObject(anyString(), eq(Pago[].class)))
            .thenReturn(pagosArray);
        
        // Act
        List<Pago> resultado = pagoService.obtenerTodosLosPagos();
        
        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());
        
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago[].class));
    }

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
        
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago[].class));
    }

    @Test
    void testObtenerTodosLosPagos_ErrorHTTP() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(Pago[].class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act
        List<Pago> resultado = pagoService.obtenerTodosLosPagos();
        
        // Assert - El servicio maneja errores y devuelve lista vacía
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "Debe retornar lista vacía en caso de error HTTP");
        
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago[].class));
    }

    @Test
    void testObtenerTodosLosPagos_ErrorConexion() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(Pago[].class)))
            .thenThrow(new ResourceAccessException("Error de conexión"));

        // Act
        List<Pago> resultado = pagoService.obtenerTodosLosPagos();
        
        // Assert - El servicio maneja errores y devuelve lista vacía
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "Debe retornar lista vacía en caso de error de conexión");
        
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago[].class));
    }

    @Test
    void testObtenerTodosLosPagos_RespuestaNull() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(Pago[].class)))
            .thenReturn(null);
        
        // Act
        List<Pago> resultado = pagoService.obtenerTodosLosPagos();
        
        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "Debe retornar lista vacía cuando la respuesta es null");
        
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago[].class));
    }
}