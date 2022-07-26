package com.finProyecto.fimoteca.web.config;

import java.time.Duration;

import org.modelmapper.ModelMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.finProyecto.fimoteca.web.utils.Constantes;
import com.finProyecto.fimoteca.web.utils.LectorPropiedades;
import com.finProyecto.fimoteca.web.utils.MensajesAccionesUsuario;
import com.finProyecto.fimoteca.web.utils.Utilidades;

@Configuration
public class AplConfig implements WebMvcConfigurer {

	/**
	 * 
	 * Dependencia que nos permite la lectura de los valores definidos en el
	 * aplication.properties
	 * 
	 * @return {@link LectorPropiedades}
	 */

	@Bean
	public LectorPropiedades lp() {
		return new LectorPropiedades();
	}
	
	/**
	 * 
	 * Dependencia que nos permite rellenar la informacion de dos objetos con
	 * estructuras simulares
	 * 
	 * @return
	 */
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	/**
	 * 
	 * Dependencia que nos permite ejecutar funcionalidades que se ultilizan en
	 * diversas zonas del codigo
	 * 
	 * @return
	 */
	
	@Bean
	public Utilidades getUtils() {
		return new Utilidades();
	}
	

	/**
	 * 
	 * Dependencia que nos permite el acceso desde la parte cliente a un servicio
	 * rest
	 * 
	 * @param restTemplateBuilder
	 * @return
	 */
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.setConnectTimeout(Duration.ofMillis(Constantes.DURATION_MILISECOND))
				.setReadTimeout(Duration.ofMillis(Constantes.DURATION_MILISECOND)).build();
	}
	
	/**
	 * 
	 * Dependencia que nos permite el acceso a registrar y consultar mensajes de
	 * aviso, operaciones completadas con exito o mensajes de error de las acciones
	 * que realiza el usuario y poder ver su estado. Solo se mostraran en el
	 * contexto de peticion request, si existe una nueva peticion request
	 * desaparecera la informacion almacenada
	 * 
	 * @return
	 */
	
	@Bean
	@RequestScope
	public MensajesAccionesUsuario getMensajes() {
		return new MensajesAccionesUsuario();
	}	
}
