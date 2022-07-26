package com.finProyecto.fimoteca.web.controller.film;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.finProyecto.fimoteca.web.negocio.exception.DuplicateException;
import com.finProyecto.fimoteca.web.negocio.exception.FileException;
import com.finProyecto.fimoteca.web.negocio.exception.NotFoundException;
import com.finProyecto.fimoteca.web.negocio.film.IFilmService;
import com.finProyecto.fimoteca.web.negocio.film.score.IScoreService;
import com.finProyecto.fimoteca.web.negocio.person.IPersonService;
import com.finProyecto.fimoteca.web.utils.Constantes;
import com.finProyecto.fimoteca.web.utils.HelperRest;
import com.finProyecto.fimoteca.web.utils.LectorPropiedades;
import com.finProyecto.fimoteca.web.utils.Utilidades;
import com.finProyecto.fimoteca.web.vo.ResponseApi;
import com.finProyecto.fimoteca.web.vo.TypePersonEnum;
import com.finProyecto.fimoteca.web.vo.dto.FilmDTO;
import com.finProyecto.fimoteca.web.vo.dto.PersonDTO;
import com.finProyecto.fimoteca.web.vo.dto.ReviewDTO;
import com.finProyecto.fimoteca.web.vo.dto.ScoreDTO;
import com.finProyecto.fimoteca.web.vo.formularios.FilmForm;

@Controller
public class FilmController {

	private final Logger logger = LoggerFactory.getLogger(FilmController.class);

	@Autowired
	LectorPropiedades lp;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	Utilidades utils;

	@Autowired
	IPersonService servicePerson;
	
	@Autowired
	IFilmService service;
	
	@Autowired
	IScoreService serviceScore;
	
	@Autowired
	RestTemplate restTemplate;

	
	/**
	 * 
	 * Visualizamos la pantalla de alta de peliculas con todos los datos necesarios
	 * para que el usuario pueda interacturar con ella
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */

	@GetMapping("/new-film")
	public String preNewFilm(Model model) throws Exception {
		logger.info("Iniciamos presentacion de la pantalla de alta de peliculas");
		String view = lp.getViewIndex();
		cargarInformacionPersonas(model);
		model.addAttribute("film", new FilmForm());
		model.addAttribute("plantilla", lp.getViewNewFilm());
		return view;
	}
	
	/**
	 * 
	 * Iniciamos el proceso de registro de una pelicula. Primero validamos que los
	 * campos obligatorios vengan rellenos. En caso de que se cumplan iniciamos el
	 * proceso de comprobar que no exista ya una pelicula con ese nombre ya
	 * registrada, en caso de que exista lo notificamos al usuario, y en caso
	 * contrario lo registramos en nuestros sistemas
	 * 
	 * 
	 * @param form
	 * @param result
	 * @param model
	 * @return
	 * @throws Exception
	 */
	
	@PostMapping("/new-film")
	public String newFilm(@Valid @ModelAttribute("film") FilmForm form, BindingResult result, Model model,  @RequestParam("image") MultipartFile file) throws Exception {	
		String view = lp.getViewIndex();
		logger.info("Inciamos el proceso de registro de registro de una pelicula");
		if (result.hasErrors()) {
			// Notificamos que se han producido errores a la hora de iniciar sesion
			logger.debug("Se han encontrado " + result.getErrorCount() + " errores de validacion");
		} else {
			String[] param = { form.getTitle() };
			try {
				FilmDTO film = utils.mapperFilm(form);
				film.setImage(file.getOriginalFilename());
				service.registrarPelicula(film);
				utils.getMensajes().agregarMensajeCorrecto(utils.leerMensajeConParametro(Constantes.PROPIEDAD_MSG_PELICULA_REGISTRADA_EXITO, param));
				
				// Iniciamos el proceso de subida del poster promocional de la pelicula
				service.subirDocumento(file);
				model.addAttribute("film", new FilmForm());
			} catch (DuplicateException ex) {
				logger.info("Se ha intendado registrar una pelicula ya registrada");
				utils.getMensajes().agregarMensajeError(utils.leerMensajeConParametro(Constantes.PROPIEDAD_MSG_PELICULA_DUPLICADA, param));
			} catch (FileException ex) {
				utils.getMensajes().agregarMensajeError(utils.leerMensajeSinParametro(Constantes.PROPIEDAD_MSG_PELICULA_IMAGEN_ERROR));
				logger.info("Se ha producido un error al registrar la pelicula");
			}
		}
		
		cargarInformacionPersonas(model);
		model.addAttribute("msgAcciones", utils.getMensajes());
		model.addAttribute("plantilla", lp.getViewNewFilm());
		return view;
	}
	
	/**
	 * 
	 * Visualizamos la pantalla de busqueda de peliculas con todos los datos necesarios
	 * para que el usuario pueda interacturar con ella
	 * 
	 * @param model
	 * @return
	 */
	
	@GetMapping("/search")
	public String preBusquedaPeliculas(Model model) {
		String view = lp.getViewIndex();
		logger.info("Inciamos el proceso de carga de informacion para la busqueda de peliculas");
		model.addAttribute("listadoAbecedario", Constantes.ABECEDDARIO);
		model.addAttribute("plantilla", lp.getViewFilmSearch());
		return view;
	}
	
	/**
	 * 
	 * Iniciamos el proceso de busqueda de una pelicula por titulo
	 * 
	 * @param model
	 * @return
	 */
	
	@PostMapping("/search")
	public String busquedaPeliculas(@RequestParam("titulo") String titulo, Model model) {
		String view = lp.getViewIndex();
		logger.info("Inciamos el proceso de busqueda de una pelicula");
		String[] param = {titulo};
		if (titulo != null && !titulo.isEmpty()) {
			Set<FilmDTO> films = service.busquedaPeliculasPorNombre(titulo);
			if (films.isEmpty()) {
				utils.getMensajes().agregarMensajeAdvertencia(utils.leerMensajeConParametro(Constantes.PROPIEDAD_MSG_PELICULA_NO_ENCONTRADA, param));
			} else {
				model.addAttribute("films", films);
			}
		} else {
			utils.getMensajes().agregarMensajeAdvertencia(utils.leerMensajeConParametro(Constantes.PROPIEDAD_MSG_PELICULA_NO_ENCONTRADA, param));
		}
		
		model.addAttribute("msgAcciones", utils.getMensajes());
		model.addAttribute("listadoAbecedario", Constantes.ABECEDDARIO);
		model.addAttribute("plantilla", lp.getViewFilmSearch());
		return view;
	}
	
	/**
	 * 
	 * Cargamos toda la informacion de una pelicula para que el usuario pueda consultarla
	 * Puntuar la pelicula si cree necesario
	 * Dejar un comentario si cree que es necesario.
	 * 
	 * @param id
	 * @param model
	 * @return
	 * @throws Exception
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-film/{id}")
	public String visualizarPelicula(@PathVariable("id") Long id, Model model) throws Exception {
		String view = lp.getViewIndex();
		logger.info("Inciamos el proceso de consulta de una pelicula");
		
		if (id == null || id == 0) {
			logger.error("Parametro id no localizado, error grave");
			throw new Exception("Error");
		}
		
		try {
			// Obtenemos la informacion de la pelicula
			FilmDTO film=service.buscarPeliculaPorId(id);
			agrupacionPersonas(model, film.getPerson());			
			
			// Obtenemos la media de puntuacion de la pelicula
			double media = serviceScore.obtenerMediaPelicula(id);
			
			// Obtenemos todas las review asociadas a la pelicula
			Map<String, Long> params = new HashMap<>();
			params.put("id", id);
			
			// Iniciamos la consulta de las reviews asociadas a la pelicula usando la api
			Collection<ReviewDTO> reviews = new ArrayList<>();
			@SuppressWarnings("rawtypes")
			ResponseEntity<ResponseApi> response = restTemplate.getForEntity(lp.getConsultarOpionesPeliculaApi(), ResponseApi.class, params);
			if (response.getBody().getStatus().equals("200")) {
				logger.debug("Encontramos peliculas asociadas");
				reviews.addAll(HelperRest.getObjectToJsonReview((ArrayList<String>)response.getBody().getData()));
			} else {
				logger.warn("El estado de la consulta a la api es " + response.getStatusCodeValue() + " - " + response.getBody().getStatus());
			}
			
			// Comrpobamos si tenemos algun usuario conectado
			boolean usuarioConectado = session.getAttribute(Constantes.CLAVE_TOKE_SESION) != null;
			
			int puntuacion = 0;
			boolean votar = true;
			ScoreDTO scoreDTO = new ScoreDTO();
			ReviewDTO reviewDTO = new ReviewDTO();
			if (usuarioConectado) {
				// Obtenemos el id del usuario conectado
				if (session.getAttribute(Constantes.ID_USER_CONECTADO) == null) {
					logger.error("Error al intentar obtener informacion del usuario almacenado en sesion");
					throw new Exception("Error");
				}
				
				long idUsuarioConectado = (int) session.getAttribute(Constantes.ID_USER_CONECTADO);
				
				// Obtenemos la puntuacion de la pelicula realizada por el usuario conectado
				ScoreDTO scoreAux = serviceScore.obtenerInformacionPuntuacionAsociadaUsuario(idUsuarioConectado, id);
				
				// Obtenemos la puntuacion que tiene el usuario sobre la pelicula
				if (scoreAux != null) {
					puntuacion = (int) scoreAux.getPoints();
					votar = false;
				}
				scoreDTO.setFilmID(id);
				scoreDTO.setUserID(idUsuarioConectado);
				reviewDTO.setFilm(id);
				reviewDTO.setUser(idUsuarioConectado);
			} else {
				votar = false;
			}
			
			model.addAttribute("review", reviewDTO);
			model.addAttribute("comentarios", reviews);
			model.addAttribute("score", scoreDTO);
			model.addAttribute("film", film);
			model.addAttribute("avg", media);
			model.addAttribute("star", puntuacion);
			model.addAttribute("votar", votar);
			model.addAttribute("plantilla", lp.getViewConsultingFilm());
		} catch (NotFoundException ex) {
			utils.getMensajes().agregarMensajeError(utils.leerMensajeSinParametro(Constantes.PROPIEDAD_MSG_PELICULA_CONSULTAR_ERROR));
			model.addAttribute("msgAcciones", utils.getMensajes());
			model.addAttribute("listadoAbecedario", Constantes.ABECEDDARIO);
			model.addAttribute("plantilla", lp.getViewFilmSearch());
		}
		return view;
	}
	
	/**
	 * 
	 * Elimina los espacios en blanco y los convierte en null
	 * 
	 * @param dataBinder
	 */

	@InitBinder
	private void initBinder(WebDataBinder dataBinder) {
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	/**
	 * 
	 * Agregamos al model las diferentes personas agrupadas por tipo
	 * 
	 * @param model
	 */
	
	private void cargarInformacionPersonas(Model model) {
		// Obtenemos todas las personas agrupadas por tipos
		Map<Long, Collection<PersonDTO>> mapTypePersonPerson = servicePerson.obtenerTodasPersonasAgrupadasPorTipoPersona();
		if (!mapTypePersonPerson.isEmpty()) {
			model.addAttribute("director", mapTypePersonPerson.get(TypePersonEnum.DIRECTOR.getId()));
			model.addAttribute("guionista", mapTypePersonPerson.get(TypePersonEnum.GUIONISTA.getId()));
			model.addAttribute("actor", mapTypePersonPerson.get(TypePersonEnum.ACTOR.getId()));
			model.addAttribute("musico", mapTypePersonPerson.get(TypePersonEnum.MUSICO.getId()));
			model.addAttribute("fotografo", mapTypePersonPerson.get(TypePersonEnum.FOTOGRAFO.getId()));
		}
	}
	
	/**
	 * 
	 * 
	 * Agregamos al model las diferentes personas agrupadas por personas en una cadena de caracteres
	 * 
	 * @param model
	 * @param persons
	 */
	
	private void agrupacionPersonas(Model model, Collection<PersonDTO> persons) {
		for (PersonDTO personDTO : persons) {
			if (personDTO.getTypePerson() == TypePersonEnum.DIRECTOR) {
				model.addAttribute("director", personDTO.getName() + " " + personDTO.getSurname());
			} else if (personDTO.getTypePerson() == TypePersonEnum.GUIONISTA) {
				String valor = (String) model.getAttribute("guionista");
				if (valor == null || valor.isEmpty()) {
					valor = personDTO.getName() + " " + personDTO.getSurname();
				} else {
					valor += ", " + personDTO.getName() + " " + personDTO.getSurname();
				}
				model.addAttribute("guionista", valor);
			} else if (personDTO.getTypePerson() == TypePersonEnum.ACTOR) {
				String valor = (String) model.getAttribute("actores");
				if (valor == null || valor.isEmpty()) {
					valor = personDTO.getName() + " " + personDTO.getSurname();
				} else {
					valor += ", " + personDTO.getName() + " " + personDTO.getSurname();
				}
				model.addAttribute("actores", valor);
			} else if (personDTO.getTypePerson() == TypePersonEnum.MUSICO) {
				String valor = (String) model.getAttribute("musico");
				if (valor == null || valor.isEmpty()) {
					valor = personDTO.getName() + " " + personDTO.getSurname();
					model.addAttribute("musico", valor);
				} else {
					valor += ", " + personDTO.getName() + " " + personDTO.getSurname();
				}
			} else if (personDTO.getTypePerson() == TypePersonEnum.FOTOGRAFO) {
				model.addAttribute("fotografo", personDTO.getName() + " " + personDTO.getSurname());
			}
		}
	}
}