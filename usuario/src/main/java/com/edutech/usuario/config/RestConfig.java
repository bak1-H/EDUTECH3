//Se crea la configuración de RestTemplate para que pueda ser utilizado en la aplicación
// Esto permite realizar llamadas HTTP a otros servicios REST, como el servicio de tipo usuario.


package com.edutech.usuario.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {
    

    //Aca se define un bean de RestTemplate para que pueda ser 
    //inyectado en otros componentes de la aplicación
    //Esto permite realizar llamadas HTTP a otros servicios REST
    //como el servicio de tipo usuario.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}