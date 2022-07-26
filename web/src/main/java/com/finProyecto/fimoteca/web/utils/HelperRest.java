package com.finProyecto.fimoteca.web.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.finProyecto.fimoteca.web.vo.dto.ReviewDTO;
import com.finProyecto.fimoteca.web.vo.dto.RolDTO;
import com.finProyecto.fimoteca.web.vo.dto.UserDTO;
import com.finProyecto.fimoteca.web.vo.formularios.LoginForm;

@Component
public class HelperRest {

	/**
	 * 
	 * Metodo encargado de configurar la cabecera que se va a enviar al servicio
	 * rest
	 * 
	 * @return
	 */

	public static HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	/**
	 * 
	 * Metodo encargado de serializar y transformar un objeto en un JSON
	 * 
	 * @param user
	 * @return
	 * @throws JsonProcessingException
	 */

	public static String getBodyLogin(LoginForm user) throws JsonProcessingException {
		ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
		return mapper.writeValueAsString(user);
	}

	/**
	 * 
	 * Metodo encargado de serializar y transformar un objeto en un JSON
	 * 
	 * @param user
	 * @return
	 * @throws JsonProcessingException
	 */

	public static String getBodyUser(UserDTO user) throws JsonProcessingException {
		ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
		return mapper.writeValueAsString(user);
	}
	
	
	public static String getBodyReview(ReviewDTO review) throws JsonProcessingException {
		ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
		return mapper.writeValueAsString(review);
	}
	

	/**
	 * 
	 * Obtenemos un listado de objetos Rol a partir de un listado json de Roles
	 * 
	 * @param listado
	 * @return
	 * @throws Exception
	 */
	
	public static Collection<RolDTO> getObjectToJsonMasivo(ArrayList<String> listado) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Collection<RolDTO> pojos = mapper.convertValue(listado, new TypeReference<List<RolDTO>>() {});
		return pojos;
	}
	
	public static Collection<ReviewDTO> getObjectToJsonReview(ArrayList<String> listado) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Collection<ReviewDTO> pojos = mapper.convertValue(listado, new TypeReference<List<ReviewDTO>>() {});
		return pojos;
	}
	
}