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
        //esta linea simula el comportamiento del RestTemplate
        // cuando se obtiene un pago por ID
        Long pagoId = 1L;
        when(restTemplate.getForObject(anyString(), eq(Pago.class)))
            .thenReturn(pagoEjemplo);
        
        //esta linea llama al método del servicio
        // que obtiene el pago por ID
        Pago resultado = pagoService.obtenerPagoPorId(pagoId);
        
        //esta linea verifica que el resultado no es nulo
        assertNotNull(resultado);
        //esta linea verifica que el ID, estado, usuarioRut y cursoId son correctos
        assertEquals(1L, resultado.getId());
        // Verifica que el estado es verdadero
        assertTrue(resultado.isEstado());
        // Verifica que el usuarioRut y cursoId son correctos
        assertEquals(12345678L, resultado.getUsuarioRut());
        assertEquals(101L, resultado.getCursoId());
        //esta linea verifica que el RestTemplate fue llamado una vez
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago.class));
    }

    @Test
    void testObtenerPagoPorId_NoEncontrado() {
        //esta linea simula el comportamiento del RestTemplate
        // cuando se intenta obtener un pago que no existe
        Long pagoId = 999L;
        when(restTemplate.getForObject(anyString(), eq(Pago.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        
        //esta linea llama al método del servicio
        // que obtiene el pago por ID
        Pago resultado = pagoService.obtenerPagoPorId(pagoId);
        
        // esta linea verifica que el resultado es null
        // ya que el pago no fue encontrado
        assertNull(resultado, "El servicio debe retornar null cuando el pago no se encuentra");
        
        //esta linea verifica que el RestTemplate fue llamado una vez
        // para intentar obtener el pago por ID
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago.class));
    }

    @Test
    void testObtenerPagoPorId_ErrorConexion() {
        //esta linea simula el comportamiento del RestTemplate
        // cuando hay un error de conexión al intentar obtener un pago
        Long pagoId = 1L;
        when(restTemplate.getForObject(anyString(), eq(Pago.class)))
            .thenThrow(new ResourceAccessException("Microservicio no disponible"));
        
        //esta linea llama al método del servicio
        // que obtiene el pago por ID
        Pago resultado = pagoService.obtenerPagoPorId(pagoId);
        
        // esta linea verifica que el resultado es null
        // ya que hubo un error de conexión
        assertNull(resultado, "El servicio debe retornar null cuando hay error de conexión");
        
        //esta linea verifica que el RestTemplate fue llamado una vez
        // para intentar obtener el pago por ID
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago.class));
    }

    @Test
    void testObtenerPagoPorId_ErrorGenerico() {
        // esta linea simula el comportamiento del RestTemplate
        // cuando ocurre un error inesperado al intentar obtener un pago
        // por ID
        // Esto puede ser útil para verificar que el servicio maneja errores genéricos
        Long pagoId = 1L;
        when(restTemplate.getForObject(anyString(), eq(Pago.class)))
            .thenThrow(new RuntimeException("Error inesperado"));
        
        // esta linea llama al método del servicio
        Pago resultado = pagoService.obtenerPagoPorId(pagoId);
        
        // esta linea verifica que el resultado es null
        // ya que hubo un error genérico
        assertNull(resultado, "El servicio debe retornar null en caso de error genérico");

        // esta linea verifica que el RestTemplate fue llamado una vez
        // para intentar obtener el pago por ID
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago.class));
    }

    // === TESTS PARA obtenerTodosLosPagos ===

    @Test
    void testObtenerTodosLosPagos_Exitoso() {
        //acá se simula el comportamiento del RestTemplate
        // cuando se obtienen todos los pagos   
        Pago pago2 = new Pago();
        pago2.setId(2L);
        pago2.setEstado(false);
        pago2.setUsuarioRut(87654321L);
        pago2.setCursoId(102L);
        
        // Se crea un arreglo de pagos que incluye el pagoEjemplo y otro pago
        // Esto simula la respuesta del microservicio de pagos
        Pago[] pagosArray = {pagoEjemplo, pago2};
        when(restTemplate.getForObject(anyString(), eq(Pago[].class)))
            .thenReturn(pagosArray);
        
        //acá se llama al método del servicio
        List<Pago> resultado = pagoService.obtenerTodosLosPagos();
        
        // esta linea verifica que el resultado no es nulo
        assertNotNull(resultado);

        // esta linea verifica que el tamaño de la lista es 2
        assertEquals(2, resultado.size());

        // esta linea verifica que los IDs de los pagos son correctos
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());
        
        // esta linea verifica que los estados de los pagos son correctos
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago[].class));
    }

    @Test
    void testObtenerTodosLosPagos_ListaVacia() {
        //acá se simula el comportamiento del RestTemplate
        // cuando se obtienen todos los pagos y no hay ninguno
        when(restTemplate.getForObject(anyString(), eq(Pago[].class)))
            .thenReturn(new Pago[0]);
        
        // acá se llama al método del servicio
        // que obtiene todos los pagos
        List<Pago> resultado = pagoService.obtenerTodosLosPagos();
        
        // esta linea verifica que el resultado no es nulo
        assertNotNull(resultado);
        // esta linea verifica que el resultado es una lista vacía
        // ya que no hay pagos disponibles
        assertTrue(resultado.isEmpty());
        
        // esta linea verifica que el RestTemplate fue llamado una vez
        // para obtener todos los pagos
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago[].class));
    }

    @Test
    void testObtenerTodosLosPagos_ErrorHTTP() {
        //acá se simula el comportamiento del RestTemplate
        // cuando se intenta obtener todos los pagos y ocurre un error HTTP
        when(restTemplate.getForObject(anyString(), eq(Pago[].class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        //llama al metodo del servicio
        List<Pago> resultado = pagoService.obtenerTodosLosPagos();
        
        // assernotNull para verificar que el resultado no es nulo
        // y que el servicio maneja el error correctamente
        assertNotNull(resultado);

        // assertTrue para verificar que el resultado es una lista vacía
        // ya que hubo un error HTTP al intentar obtener los pagos
        assertTrue(resultado.isEmpty(), "Debe retornar lista vacía en caso de error HTTP");
        
        // esta linea verifica que el RestTemplate fue llamado una vez
        // para intentar obtener todos los pagos
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago[].class));
    }

    @Test
    void testObtenerTodosLosPagos_ErrorConexion() {
        // esta linea simula el comportamiento del RestTemplate
        // cuando hay un error de conexión al intentar obtener todos los pagos
        when(restTemplate.getForObject(anyString(), eq(Pago[].class)))
            .thenThrow(new ResourceAccessException("Error de conexión"));

        // acá se llama al método del servicio
        // que obtiene todos los pagos
        List<Pago> resultado = pagoService.obtenerTodosLosPagos();
        
        // acá se verifica que el resultado no es nulo
        // y que el servicio maneja el error de conexión correctamente
        assertNotNull(resultado);

        // esta linea verifica que el resultado es una lista vacía
        // ya que hubo un error de conexión al intentar obtener los pagos
        assertTrue(resultado.isEmpty(), "Debe retornar lista vacía en caso de error de conexión");
        
        // esta linea verifica que el RestTemplate fue llamado una vez
        // para intentar obtener todos los pagos
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago[].class));
    }

    @Test
    void testObtenerTodosLosPagos_RespuestaNull() {
        //acá se simula el comportamiento del RestTemplate
        // cuando la respuesta al intentar obtener todos los pagos es null
        when(restTemplate.getForObject(anyString(), eq(Pago[].class)))
            .thenReturn(null);
        
        // llama metodo
        List<Pago> resultado = pagoService.obtenerTodosLosPagos();
        
        // esta linea verifica que el resultado no es nulo
        assertNotNull(resultado);  

        // esta linea verifica que el resultado es una lista vacía
        assertTrue(resultado.isEmpty(), "Debe retornar lista vacía cuando la respuesta es null");
        
        // esta linea verifica que el RestTemplate fue llamado una vez
        // para intentar obtener todos los pagos
        verify(restTemplate, times(1)).getForObject(anyString(), eq(Pago[].class));
    }
}