package com.finProyecto.fimoteca.web.controller.ajax.film;

import java.util.Calendar;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finProyecto.fimoteca.web.negocio.film.IFilmService;
import com.finProyecto.fimoteca.web.negocio.film.score.IScoreService;
import com.finProyecto.fimoteca.web.persistencia.film.score.Score;
import com.finProyecto.fimoteca.web.utils.Constantes;
import com.finProyecto.fimoteca.web.utils.HelperRest;
import com.finProyecto.fimoteca.web.utils.LectorPropiedades;
import com.finProyecto.fimoteca.web.utils.Utilidades;
import com.finProyecto.fimoteca.web.vo.ResponseApi;
import com.finProyecto.fimoteca.web.vo.dto.AjaxDTO;
import com.finProyecto.fimoteca.web.vo.dto.FilmDTO;
import com.finProyecto.fimoteca.web.vo.dto.ReviewDTO;
import com.finProyecto.fimoteca.web.vo.dto.ScoreDTO;

@RestController
public class FilmControllerAjax {

	private final Logger logger = LoggerFactory.getLogger(FilmControllerAjax.class);

	@Autowired
	LectorPropiedades lp;
	
	@Autowired
	Utilidades utils;

	@Autowired
	IFilmService serviceFilm;
	
	@Autowired
	IScoreService serviceScore;
	
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private HttpSession session;

	/**
	 * 
	 * Iniciamos el proceso de busqueda de peliculas mediante ajax 
	 * 
	 * @param letter
	 * @return
	 */
	
	@GetMapping("/ajax/search")
	public ResponseEntity<Object> getAllFilmByLetter(@RequestParam(name = "letter") String letter) {
		logger.info("Iniciamos la busqueda de film por la letra " + letter);

		// Obtenemos todas las peliculas cuya inicial conquerde con la indicada
		Set<FilmDTO> films = serviceFilm.buscarPeliculaPorTituloInicial(letter);

		// Enviamos la informacion en formato json
		AjaxDTO<Collection<FilmDTO>> response = new AjaxDTO<>("success", films);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	/**
	 * 
	 * Registramos la puntuacion que ha realizado el usuario
	 * 
	 * @param letter
	 * @return
	 */
	
	@PostMapping("/ajax/score")
	public ResponseEntity<Object> addScore(@RequestBody ScoreDTO scoreDTO) {
		logger.info("Iniciamos el proceso de registro de puntuacion");

		if (serviceScore.obtenerInformacionPuntuacionAsociadaUsuario(scoreDTO.getUserID(), scoreDTO.getFilmID()) != null) {
			logger.error("Error al registrar una puntuacion, el usuario ya tiene una puntuacion asignada");
			return new ResponseEntity<Object>(new AjaxDTO<>("duplicate", utils.leerMensajeSinParametro(Constantes.PROPIEDAD_MSG_AJAX_PUNTUACION_DUPLICADO)), HttpStatus.OK);
		}
		
		// Registramos la puntuacion
		Score score = utils.mapperScore(scoreDTO);
		serviceScore.registrarPuntuacion(score);
		
		// Obtenemos la media de puntuacion de la pelicula
		double avg = serviceScore.obtenerMediaPelicula(scoreDTO.getFilmID());
		
		// Enviamos el resultado en formato json
		AjaxDTO<Double> response = new AjaxDTO<>("success", avg);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	/**
	 * 
	 * Registramos una nueva review de una pelicula
	 * 
	 * @param reviewDTO
	 * @return
	 * @throws JsonProcessingException 
	 */
	
	@PostMapping("/ajax/review")
	public ResponseEntity<Object> addReview(@RequestBody ReviewDTO reviewDTO) throws Exception {
		logger.info("Iniciamos el proceso de registro de una review");
		String token = (String) session.getAttribute(Constantes.CLAVE_TOKE_SESION);
		
		// Validamos que los campos obligatorios vengan rellenos
		if ((reviewDTO.getTitle() == null || reviewDTO.getTitle().isEmpty()) || (reviewDTO.getTextReview() == null || reviewDTO.getTextReview().isEmpty())) {
			logger.warn("Campos osbligatorios no rellenos correctamente");
			return new ResponseEntity<Object>(new AjaxDTO<>("duplicate", utils.leerMensajeSinParametro(Constantes.PROPIEDAD_MSG_AJAX_COMENTARIO_CRITERIOS_NO_CUMPLIDOS)), HttpStatus.OK);
		}
		
		if (token != null && !token.isEmpty()) {
			// Creamos el cuerpo de la peticion
			reviewDTO.setDate(Calendar.getInstance().getTime());
			String authenticationBody = HelperRest.getBodyReview(reviewDTO);
		
			// Especificamos la cabecera de la peticion
			HttpHeaders authenticationHeaders = HelperRest.getHeaders();
			authenticationHeaders.set("Authorization", "Bearer " + token);
			
			HttpEntity<String> authenticationEntityUserRol = new HttpEntity<>(authenticationBody, authenticationHeaders);
			@SuppressWarnings("rawtypes")
			ResponseEntity<ResponseApi> authenticationResponseRol =  restTemplate.exchange(lp.getRegistrarOpionesPeliculaApi(), HttpMethod.POST, authenticationEntityUserRol, ResponseApi.class);
			if (!authenticationResponseRol.getBody().getStatus().equals("200")) {
				logger.error("Error al registrar una review "+authenticationResponseRol.getBody().getStatus()+" - "+authenticationResponseRol.getBody().getMsg());
				return new ResponseEntity<Object>(new AjaxDTO<>("duplicate", utils.leerMensajeSinParametro(Constantes.PROPIEDAD_MSG_AJAX_COMENTARIO_DUPLICADO)), HttpStatus.OK);
			}
			
			AjaxDTO<ReviewDTO> response = new AjaxDTO<>("success", reviewDTO);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} else {
			logger.error("Se intento registrar una review de un usuario no identificado");
			throw new Exception("Error");
		}
	}
}