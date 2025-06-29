package com.edutech.usuario.controller;

import com.edutech.usuario.model.Usuario;
import com.edutech.usuario.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;




@WebMvcTest(UsuarioController.class)
public class UsuarioTest {

    @Autowired
    private MockMvc mockMvc;  //MockMvc permite simular peticiones HTTP


    @MockitoBean //Esto permite simular el servicio UsuarioService
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper; //ObjectMapper se usa para convertir objetos a JSON y viceversa

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach //Esto sirve para inicializar los datos antes de cada prueba
    void setUp(){
        
        usuario1 = new Usuario();
        usuario1.setRut(12345678L);
        usuario1.setDv("9");
        usuario1.setNombre("Juan");
        usuario1.setEmail("juanperez@gmail.com");
        usuario1.setContrasena("naruto456");
        usuario1.setFechaRegistro(null); //Se deja nulo por que el servicio lo asigna automáticamente
        usuario1.setTipoUsuarioId(1L); //Asumiendo que el tipo de usuario es 1 


        usuario2 = new Usuario();
        usuario2.setRut(87654321L);
        usuario2.setDv("8");
        usuario2.setNombre("Maria");
        usuario2.setEmail("mariaperez@gmail.com");
        usuario2.setContrasena("naruto123");
        usuario2.setFechaRegistro(null); //Se deja nulo por que el servicio lo asigna automáticamente
        usuario2.setTipoUsuarioId(2L); //Asumiendo que el tipo de usuario es 2
    }


    //Prueba para Get de un usuario por RUT
    // Esta prueba verifica que al hacer un GET al endpoint /api/usuarios/{rut}
    // se retorne el usuario correspondiente al RUT especificado.
    // Utiliza Mockito para simular el comportamiento del servicio UsuarioService.


    @Test
    void testObtenerTodosUsuarios() throws Exception {
        //Cuando se llame al método obtenerTodos del servicio, devolverá una lista con los
        //dos usuarios que hemos creado
        ArrayList<Usuario> ListaUsuario = new ArrayList<>(Arrays.asList(usuario1, usuario2));
        when(usuarioService.obtenerTodos()).thenReturn(ListaUsuario);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk()) //Esperamos un estado 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //Esperamos que el contenido sea JSON
                .andExpect(jsonPath("$", hasSize(2))) //Esperamos que la lista tenga 2 elementos
                .andExpect(jsonPath("$[0].rut", is(12345678))) //Esperamos que el primer usuario tenga el RUT 12345678
                .andExpect(jsonPath("$[1].rut", is(87654321))); //Esperamos que el segundo usuario tenga el RUT 87654321

    }
    
    
    //Prueba para post de un usuario
    // Esta prueba verifica que al hacer un POST al endpoint /api/usuarios
    // se guarde correctamente un usuario y se retorne el usuario guardado.
    // Utiliza Mockito para simular el comportamiento del servicio UsuarioService.
    @Test
    void guardarusuario_retornausuarioguardado() throws Exception {

        when(usuarioService.guardarUsuario(any(Usuario.class))).thenReturn(usuario1);

        mockMvc.perform(post("/api/usuarios")
                

                //Realizamos una petición POST al endpoint /api/usuarios
                .contentType(MediaType.APPLICATION_JSON)

                //Indicamos que el contenido de la petición es JSON
                .content(objectMapper.writeValueAsString(usuario1))) //Convertimos el usuario a JSON
                
                //Convertimos el usuario a JSON y lo enviamos en el cuerpo de la petición
                .andExpect(status().isOk()) //Esperamos un estado 200 OK
                
                //Esperamos que el contenido de la respuesta sea JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rut", is(12345678))) //Esperamos que el RUT del usuario guardado sea 12345678
                .andExpect(jsonPath("$.nombre", is("Juan"))); //Esperamos que el nombre del usuario guardado sea "Juan"
    }


        
    //Prueba para delete de un usuario
    // Esta prueba verifica que al hacer un DELETE al endpoint /api/usuarios/{rut}
    // se elimine correctamente el usuario correspondiente al RUT especificado.

    @Test
    void eliminarUsuario_retornaTrue() throws Exception {
        //Simulamos que el servicio elimina el usuario sin errores
        //Cuando se llame al método obtenerPorRut del servicio, devolverá un Optional con el usuario1
        //Esto simula que el usuario existe y puede ser eliminado
        //Si el usuario no existiera, se lanzaría una excepción y la prueba fallaría
        when(usuarioService.obtenerPorRut(anyLong())).thenReturn(java.util.Optional.of(usuario1));


        //Simulamos que el servicio elimina el usuario sin errores
        //Cuando se llame al método eliminarUsuario del servicio, no hará nada (void)
        //Esto simula que la eliminación se realiza correctamente0  
        mockMvc.perform(delete("/api/usuarios/{rut}", 12345678L))
        .andExpect(content().string("El Usuario ha sido eliminado correctamente.")); //Esperamos que el mensaje de éxito sea el correcto
     
    }

    @Test 
    void obtenerusuario_por_rut_retornaUsuario() throws Exception {
        when(usuarioService.obtenerPorRut(12345678L)).thenReturn(java.util.Optional.of(usuario1)); //Simula el comportamiento del servicio al devolver el usuario con el RUT especificado
        mockMvc.perform(get("/api/usuarios/{rut}", 12345678L)); //

        mockMvc.perform(get("/api/usuarios/{rut}", 12345678L))
                .andExpect(status().isOk()) //Esperamos un estado 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //Esperamos que el contenido sea JSON
                .andExpect(jsonPath("$.rut", is(12345678))) //Esperamos que el RUT del usuario sea 12345678
                .andExpect(jsonPath("$.nombre", is("Juan"))); //Esperamos que el nombre del usuario sea "Juan"     
    }


    


    
    
}
