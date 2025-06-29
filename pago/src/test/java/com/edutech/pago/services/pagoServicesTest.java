package com.edutech.pago.services;

import com.edutech.pago.model.Pago;
import com.edutech.pago.repository.PagoRepository;
import com.edutech.pago.service.PagoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

//ExtendWith es una anotación de JUnit 5 que permite usar 
//Mockito para pruebas unitarias

@ExtendWith(MockitoExtension.class) 
public class pagoServicesTest {


    @Mock
    private PagoRepository pagoRepository;


    @InjectMocks
    private PagoService pagoService;
    
    private Pago pago1;
    private Pago pago2;


@BeforeEach
void setUp() {
    pago1 = new Pago();
    pago1.setId(1L);
    pago1.setUsuarioRut(12345678L);
    pago1.setCursoId(101L);
    pago1.setEstado(true);

    pago2 = new Pago();
    pago2.setId(2L);
    pago2.setUsuarioRut(87654321L);  
    pago2.setCursoId(102L);
    pago2.setEstado(false);
}

//Este test sirve para obtener la lista de pagos que fue creada con pago1 y pago2.

    @Test 
    void obtenerTodos_retornaListaPagos(){
        // Arrange
        ArrayList<Pago> pagosTest = new ArrayList<>(Arrays.asList(pago1, pago2));
        when(pagoRepository.findAll()).thenReturn(pagosTest);

        // Act
        List<Pago> resultadoObtenido = pagoService.obtenerTodos();

        // Assert
        assertNotNull(resultadoObtenido);
        assertEquals(2, resultadoObtenido.size(), "La Lista deberia contener 2 pagos");
        
        // Verificar propiedades del primer pago
        assertEquals(1L, resultadoObtenido.get(0).getId());
        assertEquals(12345678L, resultadoObtenido.get(0).getUsuarioRut());
        assertEquals(101L, resultadoObtenido.get(0).getCursoId());
        assertTrue(resultadoObtenido.get(0).isEstado());
        
        // Verificar propiedades del segundo pago
        assertEquals(2L, resultadoObtenido.get(1).getId());
        assertEquals(87654321L, resultadoObtenido.get(1).getUsuarioRut());
        assertEquals(102L, resultadoObtenido.get(1).getCursoId());
        assertFalse(resultadoObtenido.get(1).isEstado());

        verify(pagoRepository, times(1)).findAll();
    }

//Este test sirve para obtener pagos por id.
@Test
void obtenerPagoPorId_retornaPago() {
    // Arrange
    when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago1));

    // Act
    Optional<Pago> resultado = pagoService.obtenerPorId(1L);

    // Assert
    assertTrue(resultado.isPresent(), "El pago deberia estar presente");
    assertEquals(1L, resultado.get().getId());
    assertEquals(12345678L, resultado.get().getUsuarioRut());  // ← LÍNEA 101 que falla
    assertEquals(101L, resultado.get().getCursoId());
    assertTrue(resultado.get().isEstado());

    verify(pagoRepository, times(1)).findById(1L);
}
//Este test sirve para guardar pagos.
@Test
void guardarPago_retornaPagoCreado() {
    when(pagoRepository.save(any(Pago.class))).thenReturn(pago1);

    Pago resultado = pagoService.guardarPago(pago1);

    assertNotNull(resultado, "El resultado no deberia ser nulo");
    assertEquals(pago1, resultado, "El pago guardado deberia ser el mismo que el pago 1");

    verify(pagoRepository, times(1)).save(pago1);
}

//Este test sirve para eliminar pagos por Id.
    @Test
    void obtenerPagoPorId_noExiste_retornaEmpty() {
        // Arrange
        when(pagoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Pago> resultado = pagoService.obtenerPorId(999L);

        // Assert
        assertFalse(resultado.isPresent(), "El pago no deberia estar presente");
        verify(pagoRepository, times(1)).findById(999L);
    }

//Este test sirve para obtener pagos por usuario Rut.
@Test
void obtenerPorUsuarioRut_retornaPagosPorUsuario() {
    // Arrange
    List<Pago> pagosUsuario = Arrays.asList(pago1);  // pago1 ahora tiene usuarioRut = 12345678L
    when(pagoRepository.findByUsuarioRut(12345678L)).thenReturn(pagosUsuario);

    // Act
    List<Pago> resultado = pagoService.obtenerPorUsuarioRut(12345678L);

    // Assert
    assertNotNull(resultado);
    assertEquals(1, resultado.size());
    assertEquals(12345678L, resultado.get(0).getUsuarioRut());
    verify(pagoRepository, times(1)).findByUsuarioRut(12345678L);
}

//Este test sirve para obtener pagos por curso Id.

    @Test
    void obtenerPorCursoId_retornaPagosPorCurso() {
        // Arrange
        List<Pago> pagosCurso = Arrays.asList(pago1);
        when(pagoRepository.findByCursoId(101L)).thenReturn(pagosCurso);

        // Act
        List<Pago> resultado = pagoService.obtenerPorCursoId(101L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(101L, resultado.get(0).getCursoId());
        verify(pagoRepository, times(1)).findByCursoId(101L);
    }

//Este test sirve para eliminar pagos por Id.
    @Test 
    void eliminarPago_eliminaCorrectamente() {
        // Arrange
        doNothing().when(pagoRepository).deleteById(1L);

        // Act & Assert - No debería lanzar excepción
        assertDoesNotThrow(() -> {
            pagoService.eliminarPago(1L);
        });

        verify(pagoRepository, times(1)).deleteById(1L);
    }


//Este test sirve para eliminar pagos por Id, pero en este caso se lanza una excepción.
@Test
    void eliminarPago_conError_lanzaExcepcion() {
        // Arrange
        doThrow(new RuntimeException("Error al eliminar")).when(pagoRepository).deleteById(1L);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            pagoService.eliminarPago(1L);
        });

        verify(pagoRepository, times(1)).deleteById(1L);
    }

//Este test sirve para verificar que el servicio maneja correctamente listas vacías.
@Test
void obtenerPorUsuarioRut_listaVacia_retornaListaVacia() {
    // Arrange
    List<Pago> pagosVacios = new ArrayList<>();
    when(pagoRepository.findByUsuarioRut(99999999L)).thenReturn(pagosVacios);

    // Act
    List<Pago> resultado = pagoService.obtenerPorUsuarioRut(99999999L);

    // Assert
    assertNotNull(resultado);
    assertEquals(0, resultado.size());
    assertTrue(resultado.isEmpty());
    verify(pagoRepository, times(1)).findByUsuarioRut(99999999L);
}

//Este test sirve para verificar que el servicio maneja correctamente listas vacías para cursos.
@Test
void obtenerPorCursoId_listaVacia_retornaListaVacia() {
    // Arrange
    List<Pago> pagosVacios = new ArrayList<>();
    when(pagoRepository.findByCursoId(99999L)).thenReturn(pagosVacios);

    // Act
    List<Pago> resultado = pagoService.obtenerPorCursoId(99999L);

    // Assert
    assertNotNull(resultado);
    assertEquals(0, resultado.size());
    assertTrue(resultado.isEmpty());
    verify(pagoRepository, times(1)).findByCursoId(99999L);
}


}