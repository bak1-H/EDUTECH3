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

//Este test sirve para obtener la lista de pagos que fue creada con pago1 y pago2,
// Verifica que el método findAll del repositorio de pagos sea llamado una vez y 
//que la lista de pagos obtenida no sea nula y contenga los dos pagos esperados.


    @Test 
    void obtenerTodos_retornaListaPagos(){
        ArrayList<Pago> pagosTest = new ArrayList<>(Arrays.asList(pago1, pago2));
        when(pagoRepository.findAll()).thenReturn(pagosTest);
        List<Pago> resultadoObtenido = pagoService.obtenerTodos();
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
//Verifica que el método findById del repositorio de pagos sea llamado una vez y
//que el pago obtenido sea el mismo que el pago1 creado en setUp().
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
// Llama al método deleteById del repositorio de pagos y verifica que se haya llamado una vez.
    @Test
    void obtenerPagoPorId_noExiste_retornaEmpty() {
        when(pagoRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<Pago> resultado = pagoService.obtenerPorId(999L);
        assertFalse(resultado.isPresent(), "El pago no deberia estar presente");
        verify(pagoRepository, times(1)).findById(999L);
    }

//Este test sirve para obtener pagos por usuario Rut.
//Recorre la lista de pagos y verifica que el usuarioRut del pago coincida con el usuarioRut buscado.
//Si encuentra un pago con el usuarioRut buscado, lo agrega a la lista de resultados.
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
//reccore la lista de pagos y verifica que el cursoId del pago coincida con el cursoId buscado.
//Si encuentra un pago con el cursoId buscado, lo agrega a la lista de resultados

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
//llama al método deleteById del repositorio de pagos y verifica que se haya llamado una vez.
    @Test 
    void eliminarPago_eliminaCorrectamente() {
        doNothing().when(pagoRepository).deleteById(1L);
        assertDoesNotThrow(() -> {
            pagoService.eliminarPago(1L);
        });

        verify(pagoRepository, times(1)).deleteById(1L);
    }


//Este test sirve para eliminar pagos por Id, pero en este caso se lanza una excepción.
//Significa que al intentar eliminar un pago con un Id que no existe, se lanza una excepción.
@Test
    void eliminarPago_conError_lanzaExcepcion() {
        doThrow(new RuntimeException("Error al eliminar")).when(pagoRepository).deleteById(1L);
        assertThrows(RuntimeException.class, () -> {
            pagoService.eliminarPago(1L);
        });

        verify(pagoRepository, times(1)).deleteById(1L);
    }


}