package com.edutech.pago.controller;

import com.edutech.pago.service.PagoService;
import com.edutech.pago.model.Pago;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


@WebMvcTest(PagoController.class)
public class pagoControllerTest {

@Autowired
private MockMvc mockMvc;

@MockBean
private PagoService pagoService;

@Autowired
private ObjectMapper objectMapper;

private Pago pago1;
private Pago pago2;



//SE CREAN LOS OBJETOS PARA UTILIZAR EN EL TEST
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




//Test para obtener pago retonando una lista.

@Test
void obtenerPagos_retornarListapagos() throws Exception {

    //Se simula el comportamiento del servicio de pago para retornar una lista de pagos.
    //En este caso, se crean dos pagos: pago1 y pago2.
    //mockMvc es un objeto que permite simular peticiones HTTP y verificar las respuestas.
    //Cuando se realiza una petición GET a "/api/pagos", se espera que el servicio
    //de pago retorne una lista que contiene los dos pagos creados anteriormente.
    //Finalmente, se verifica que la respuesta tenga un estado 200 OK y que el contenido
    //sea de tipo JSON, además de verificar que la lista contenga exactamente 2 elementos.

    List<Pago> pagos = Arrays.asList(pago1, pago2);
    when(pagoService.obtenerTodos()).thenReturn(pagos);
    mockMvc.perform(get("/api/pagos"))  // 
            .andExpect(status().isOk())  //
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)));

    verify(pagoService, times(1)).obtenerTodos();
}


//Este test sirve para guardar pago y retorna el pago creado.

@Test
void guardarPago_retornaPagoCreado() throws Exception {

    //Esta linea sirve para simular el comportamiento del servicio de pago.
    //Cuando se llama al método guardarPago del servicio de pago con cualquier objeto Pago,
    //se espera que retorne el objeto pago1.
    //Luego, se realiza una petición POST a "/api/pagos" con el objeto pago1 en formato JSON.
    //Finalmente, se verifica que la respuesta tenga un estado 200 OK, que el contenido sea de tipo JSON,
    //y que el JSON devuelto contenga los valores esperados para id y usuarioRut.
    //También se verifica que el método guardarPago del servicio de pago haya sido llamado una vez
    when(pagoService.guardarPago(any(Pago.class))).thenReturn(pago1);
    mockMvc.perform(post("/api/pagos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pago1)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.usuarioRut").value(12345678L));

    verify(pagoService, times(1)).guardarPago(any(Pago.class));
}


//Este test sirve para obtener pago por Id y mostrar el pago encontrado.
@Test
void obtenerPagoporId_retornaPagoPorId() throws Exception {
    //Esta linea sirve para simular el comportamiento del servicio de pago.
    when(pagoService.obtenerPorId(1L)).thenReturn(Optional.of(pago1));

    //Luego, se realiza una petición GET a "/api/pagos/{id}" con el id 1.
    //Finalmente, se verifica que la respuesta tenga un estado 200 OK, que el contenido sea de tipo JSON,
    //y que el JSON devuelto contenga los valores esperados para id, usuarioRut, cursoId y estado.
    //También se verifica que el método obtenerPorId del servicio de pago haya sido llamado una
    //vez con el id 1.

    mockMvc.perform(get("/api/pagos/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.usuarioRut").value(12345678L))
            .andExpect(jsonPath("$.cursoId").value(101L))
            .andExpect(jsonPath("$.estado").value(true));

    verify(pagoService, times(1)).obtenerPorId(1L);
}


//Este test sirve para verificar el comportamiento cuando no se encuentra un pago por ID.
@Test
void obtenerPagoPorId_noExiste_retornaEmpty() throws Exception {
    //esta linea sirve para simular el comportamiento del servicio de pago.
    //Cuando se llama al método obtenerPorId del servicio de pago con un ID que no
    Long pagoId = 999L;
    when(pagoService.obtenerPorId(pagoId)).thenReturn(Optional.empty());

    // esta line realiza una petición GET a "/api/pagos/{id}" con el ID 999L.
    // Luego, se verifica que la respuesta tenga un estado 404 Not Found,
    mockMvc.perform(get("/api/pagos/{id}", pagoId))
            .andExpect(status().isNotFound()) // ← Cambiar de isOk() a isNotFound()
            .andExpect(content().string("")); // O verificar el mensaje de error apropiado

    verify(pagoService, times(1)).obtenerPorId(pagoId);
}


//Este test sirve para obtener pagos por Rut y retornar los pagos encontrados.

@Test
void obtenerpagoPorUsuarioRut_retornaPagosPorUsuarioRut() throws Exception {

    //Esta linea sirve para simular el comportamiento del servicio de pago.
    //Cuando se llama al método obtenerPorUsuarioRut del servicio de pago con el rut 12345678L,
    //se espera que retorne una lista que contiene el pago1.
    //Luego, se realiza una petición GET a "/api/pagos/usuario/{usuarioRut}" con el rut 12345678L.
    //Finalmente, se verifica que la respuesta tenga un estado 200 OK, que el contenido sea de tipo JSON,
    //y que la lista devuelta contenga exactamente 1 elemento.
    //También se verifica que el JSON del primer elemento de la lista contenga los valores esperados
    //para id, usuarioRut, cursoId y estado.
    List<Pago> pagosUsuario = Arrays.asList(pago1);
    when(pagoService.obtenerPorUsuarioRut(12345678L)).thenReturn(pagosUsuario);
    mockMvc.perform(get("/api/pagos/usuario/{usuarioRut}", 12345678L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].usuarioRut").value(12345678L))
            .andExpect(jsonPath("$[0].cursoId").value(101L))
            .andExpect(jsonPath("$[0].estado").value(true));

    verify(pagoService, times(1)).obtenerPorUsuarioRut(12345678L);
}


//Este test sirve para obtener un pago mediante el id del curso y retorna los pagos encontrados.

@Test
void obtenerCursosporId_retornaPagosPorCursoId() throws Exception {
    //Esta linea sirve para simular el comportamiento del servicio de pago.
    //Cuando se llama al método obtenerPorCursoId del servicio de pago con el id 101L,
    //se espera que retorne una lista que contiene el pago1.
    List<Pago> pagosCurso = Arrays.asList(pago1);
    when(pagoService.obtenerPorCursoId(101L)).thenReturn(pagosCurso);

    //Luego, se realiza una petición GET a "/api/pagos/curso/{cursoId}" con el id 101L.
    //Finalmente, se verifica que la respuesta tenga un estado 200 OK, que el contenido
    //sea de tipo JSON, y que la lista devuelta contenga exactamente 1 elemento.
    //También se verifica que el JSON del primer elemento de la lista contenga los valores esperados
    //para id, usuarioRut, cursoId y estado.

    mockMvc.perform(get("/api/pagos/curso/{cursoId}", 101L)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].usuarioRut").value(12345678L))
            .andExpect(jsonPath("$[0].cursoId").value(101L))
            .andExpect(jsonPath("$[0].estado").value(true));

    verify(pagoService, times(1)).obtenerPorCursoId(101L);
}

//este Test sirve para eliminar un pago y verificar que el método sea llamado correctamente.

@Test 
void eliminarPago_retornaNoContent() throws Exception {

    //Esta línea sirve para simular el comportamiento del servicio de pago.
    //Cuando se llama al método eliminarPago del servicio de pago con el id 1.
    //Se verifica que no se lance ninguna excepción y que el método del servicio sea llamado.

    mockMvc.perform(delete("/api/pagos/1"))
            .andExpect(status().isNoContent()); // ← No espera contenido, solo el estado 204 No Content

    // Verificar que el método eliminarPago fue llamado exactamente una vez con el id correcto
    verify(pagoService, times(1)).eliminarPago(1L);
}

}




