package com.finProyecto.fimoteca.web.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finProyecto.fimoteca.web.utils.Constantes;
import com.finProyecto.fimoteca.web.utils.HelperRest;
import com.finProyecto.fimoteca.web.utils.LectorPropiedades;
import com.finProyecto.fimoteca.web.utils.Utilidades;
import com.finProyecto.fimoteca.web.vo.ResponseApi;
import com.finProyecto.fimoteca.web.vo.dto.RolDTO;
import com.finProyecto.fimoteca.web.vo.dto.UserDTO;
import com.finProyecto.fimoteca.web.vo.formularios.LoginForm;
import com.finProyecto.fimoteca.web.vo.formularios.SignInForm;

@Controller
public class UserController {

	private final static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	LectorPropiedades lp;
	
	@Autowired
	Utilidades utils;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private HttpSession session;
	
	
	/**
	 * 
	 * Visualizamos la pantalla de login con todos los datos necesarios para que el
	 * usuario pueda interacturar con ella
	 * 
	 * @param model
	 * @return String view
	 */

	@GetMapping("/login")
	public String login(Model model) throws Exception{
		logger.info("Iniciamos la presentacion de la pantalla de login y cargamos la informacion necesaria que necesita dicha pantalla");
		if(session.getAttribute(Constantes.CLAVE_TOKE_SESION) == null) {
			// Mostamos la pantalla de login en caso de que no exista usuario logado
			model.addAttribute("user", new LoginForm());
			model.addAttribute("plantilla", lp.getViewLogin());	
		}
		return lp.getViewIndex();
	}

	/**
	 * 
	 * Iniciamos el proceso de autentificacion en la aplicacion. Primero validamos
	 * que los campos obligatorios esten rellenos. Comprobamos si el token esta
	 * almacenado en sesion, en caso de que no enviamos el usuario y password a la
	 * api rest para iniciar el proceso de autentificacion.
	 * 
	 * @param userLoginForm
	 * @param result
	 * @param model
	 * @return String view
	 * @throws JsonProcessingException
	 */
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("user") LoginForm userLoginForm, BindingResult result, Model model) throws Exception {
		logger.info("Iniciamos el proceso de inicio de sesion");
		String view = lp.getViewIndex();

		if (result.hasErrors()) {
			// Notificamos que se han producido errores a la hora de iniciar sesion
			logger.debug("Se han encontrado " + result.getErrorCount() + " errores de validacion");
			model.addAttribute("plantilla", lp.getViewLogin());
		} else {
			// Obtenemos de la sesion el token
			String token = (String) session.getAttribute(Constantes.CLAVE_TOKE_SESION);
			if (token == null) {
				//Incluimos al user la fecha de login
				logger.warn("El token no existe, iniciamos el proceso de crear el token");
				
				// Creamos un objeto json con toda la informacion a enviar al api rest
				UserDTO userDTO = UserDTO.builder().username(userLoginForm.getUsername()).lastLogin(LocalDateTime.now()).build();
				
				// Convertimos el obj usuario en un json
				String authenticationBody = HelperRest.getBodyLogin(userLoginForm);
				String authenticationBodyUser = HelperRest.getBodyUser(userDTO);

				// Especificamos la cabecera de la peticion
				HttpHeaders authenticationHeaders = HelperRest.getHeaders();	
				
				// Creamos el cuerpo de las diferentes peticiones
				HttpEntity<String> authenticationEntityLogin = new HttpEntity<>(authenticationBody, authenticationHeaders);
			
				// Llamamos a la api rest para comprobar si el usuario y password son correctos
				logger.info("Llamada al servicio rest para la autentificacion en el sistema");
				
				ResponseEntity<ResponseApi> authenticationResponse = restTemplate.exchange(lp.getLoginRestApi(), HttpMethod.POST, authenticationEntityLogin, ResponseApi.class);				
				if (authenticationResponse.getBody().getStatus().equals("401")) {
					logger.warn("Usuario/password erroneos");
					utils.getMensajes().agregarMensajeError(utils.leerMensajeSinParametro(Constantes.PROPIEDAD_MSG_USUARIO_PASSWORD_ERRONEOS));
					model.addAttribute("plantilla", lp.getViewLogin());
				} else if (authenticationResponse.getBody().getStatus().equals("101")) {
					logger.error("Se ha producido un error inexperado en la api rest al intentar iniciar sesion");
					throw new Exception(authenticationResponse.getBody().getMsg());
				} else {
					// Obtenemos el token del usuario conectado
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, String> responseToken = (LinkedHashMap<String, String>) authenticationResponse.getBody().getData();
					String tokenUser = responseToken.get("token");
					
					// Agregamos el token al headers
					authenticationHeaders.set("Authorization", "Bearer " + tokenUser);
					
					// Llamamos a la api para obtener el rol de usuario conectado
					logger.info("Llamamos al servicio rest para obtener el rol del usuario logeado");
					HttpEntity<String> authenticationEntityUserRol = new HttpEntity<>(authenticationBody, authenticationHeaders);
					ResponseEntity<ResponseApi> authenticationResponseRol =  restTemplate.exchange(lp.getRolUsuarioConectadoApi(), HttpMethod.POST, authenticationEntityUserRol, ResponseApi.class);
					if (authenticationResponseRol.getBody().getStatus().equals("404") || authenticationResponse.getBody().getStatus().equals("101")) {
						logger.error("Rol de usuario no encontrado o error inexperado en la api rest");
						throw new Exception(authenticationResponseRol.getBody().getMsg());
					}
					
					// Actualizamos la hora de login del usuario
					HttpEntity<String> authenticationEntityUserUpdateDateLogin = new HttpEntity<>(authenticationBodyUser, authenticationHeaders);
					logger.info("Actualizamos la hora de conexion del login del usuario conectado");
					ResponseEntity<ResponseApi> authenticationResponseUpdateLogin = restTemplate.exchange(lp.getActualizarLastLoginApi(),HttpMethod.POST, authenticationEntityUserUpdateDateLogin, ResponseApi.class);
					if (authenticationResponseUpdateLogin.getBody().getStatus().equals("101")) {
						logger.error("No se pudo actualizar la ultima hora de conexion del usuario al logearse");
						throw new Exception(authenticationResponseUpdateLogin.getBody().getMsg());
					}
					
					// Obtenemos el id del usuario conectado
					logger.info("Obtenemos el id del usuario conectado");
					HttpEntity<String> authenticationEntityUserID = new HttpEntity<>(authenticationBody, authenticationHeaders);
					ResponseEntity<ResponseApi> authenticationResponseIDUserLogin = restTemplate.exchange(lp.getIdUsuarioConectadoApi(),HttpMethod.POST, authenticationEntityUserID, ResponseApi.class);
					if (authenticationResponseIDUserLogin.getBody().getStatus().equals("101")) {
						logger.error("No se pudo obtener el id del usuario conectado");
						throw new Exception(authenticationResponseUpdateLogin.getBody().getMsg());
					}
					
					// Almacenamos en sesion el rol del usuario, el token y el nombre del usuario
					String responseRol = (String)  authenticationResponseRol.getBody().getData();
					int responseID = (Integer)  authenticationResponseIDUserLogin.getBody().getData();
					session.setAttribute(Constantes.CLAVE_TOKE_SESION, responseToken.get("token"));
					session.setAttribute(Constantes.USERNAME_SESION, userDTO.getUsername());
					session.setAttribute(Constantes.ROL_USER_SESION, responseRol);
					session.setAttribute(Constantes.ID_USER_CONECTADO, responseID);
					logger.info("Inicio de sesion completado con exito");
				}
			} else {
				logger.debug("Ya tiene sesion abierta el usuario");
			}
		}
		model.addAttribute("msgAcciones", utils.getMensajes());
		return view;
	}
	
	/**
	 * 
	 * Visualizamos la pantalla de registro de usuario con todos los datos
	 * necesarios para que el usuario pueda interactuar con ella
	 * 
	 * @param model
	 * @return
	 */
	
	@GetMapping("/new-user")
	public String registrarUsuario(Model model) throws Exception {
		logger.info("Iniciamos la presentacion de la pantalla de registro de usuarios y cargamos la informacion necesaria que necesita dicha pantalla");
		
		// Inicializamos el objeto del formulario
		SignInForm form =  new SignInForm();
		
		// Comprobamos si tenemos usuario conectado y tiene rol de admin
		boolean usuarioConectado = session.getAttribute(Constantes.CLAVE_TOKE_SESION) != null;
		boolean isAdmin = session.getAttribute(Constantes.ROL_USER_SESION) != null && session.getAttribute(Constantes.ROL_USER_SESION).equals(lp.getValorPropiedad(Constantes.PERFIL_ADMIN_APP));
		
		if (usuarioConectado && isAdmin) {
			logger.info("Obtenemos los roles registrados en nuestros sistemas");
			model.addAttribute("roles", obtenerRolesApp());
		} else {
			logger.info("Asignamos el rol por defecto");
			form.setRol(lp.getValorPropiedad(Constantes.PERFIL_DEFECTO_APP));
		}
		
		model.addAttribute("user", form);
		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("plantilla", lp.getViewSingIn());
		return lp.getViewIndex();
	}
	
	
	/**
	 * 
	 * Iniciamos el proceso de registro de un usuario
	 * 
	 * @param form
	 * @param result
	 * @param model
	 * @return
	 * @throws JsonProcessingException
	 */
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/new-user")
	public String registrarUsuario(@Valid @ModelAttribute("user") SignInForm form, BindingResult result, Model model) throws Exception {
		logger.info("Iniciamos el proceso de registro de usuario");
		String view = lp.getViewIndex();
		
		// Comprobamos si el usuario es adminitrador o no
		boolean isAdmin = session.getAttribute(Constantes.ROL_USER_SESION) != null && session.getAttribute(Constantes.ROL_USER_SESION).equals(lp.getValorPropiedad(Constantes.PERFIL_ADMIN_APP));
		boolean usuarioConectado = session.getAttribute(Constantes.CLAVE_TOKE_SESION) != null;
		if (result.hasErrors()) {
			// Notificamos que se han producido errores a la hora de registar un usuario
			logger.debug("Se han encontrado " + result.getErrorCount() + " errores de validacion");
			model.addAttribute("plantilla", lp.getViewSingIn());
		} else {
			// Agregamos la informacion al objeto
			UserDTO userDTO = utils.mapperUser(form);
			
			// Agregamos el rol al usuario
			if (usuarioConectado && isAdmin) {
				userDTO.setRol(RolDTO.builder().id(Long.parseLong(form.getRol())).build());
			} else if ((usuarioConectado && !isAdmin) || !usuarioConectado) {
				userDTO.setRol(RolDTO.builder().name(lp.getValorPropiedad(Constantes.PERFIL_DEFECTO_APP)).build());
			}
			
			// Especificamos la cabecera de la peticion
			HttpHeaders authenticationHeaders = HelperRest.getHeaders();
			
			// Especificamos el cuerpo de la peticion
			String authenticationBody = HelperRest.getBodyUser(userDTO);
			
			// Unimos la cabecera y el cuerpo de la peticion
			HttpEntity<String> authenticationEntity = new HttpEntity<>(authenticationBody, authenticationHeaders);
			
			// Iniciamos la llamada a la ApiRest
			ResponseEntity<ResponseApi> authenticationResponse = restTemplate.exchange(lp.getSingInRestApi(), HttpMethod.POST, authenticationEntity, ResponseApi.class);
			if (authenticationResponse.getBody().getStatus().equals(String.valueOf(HttpStatus.OK.value()))) {
				logger.debug("Usuario registrado correctamente");
				
				// Notificamos que el usuario se ha creado correctamente
				String[] param = { userDTO.getUsername() };
				utils.getMensajes().agregarMensajeCorrecto(utils.leerMensajeConParametro(Constantes.PROPIEDAD_MSG_USUARIO_REGISTRADO_EXITO, param));
				model.addAttribute("plantilla", lp.getViewLogin());
			} else if(authenticationResponse.getBody().getStatus().equals(String.valueOf(HttpStatus.UNAUTHORIZED.value()))){
				logger.warn("Usuario con nombre ya registrado");
				
				// Notificamos que el usuario que intenta registrar ya esta registrado en el sistema
				String[] param = { userDTO.getUsername() };
				utils.getMensajes().agregarMensajeError(utils.leerMensajeConParametro(Constantes.PROPIEDAD_MSG_USUARIO_DUPLICADO, param));
				model.addAttribute("plantilla", lp.getViewSingIn());
			} else if (authenticationResponse.getBody().getStatus().equals("101")) {
				// Lanzamos una excepcion no se puede crear el usuario
				logger.warn("Se ha producido un error inexperado a la hora de crear usuario");
				throw new Exception(authenticationResponse.getBody().getMsg());
			}
		}
		
		if (isAdmin) {
			logger.info("Obtenemos los roles registrados en nuestros sistemas");
			model.addAttribute("roles", obtenerRolesApp());
			
			// Enviamos a la pantalla de registro de usuarios
			model.addAttribute("plantilla", lp.getViewSingIn());
			model.addAttribute("user", new SignInForm());
		}
		
		model.addAttribute("msgAcciones", utils.getMensajes());
		model.addAttribute("isAdmin", isAdmin);
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
	
	private Collection<RolDTO> obtenerRolesApp() throws Exception {
		// Generamos la cabecera
		HttpHeaders headers = HelperRest.getHeaders();
		headers.set("Authorization", "Bearer " + session.getAttribute(Constantes.CLAVE_TOKE_SESION));

		// Obtenemos todos los roles registramos en el sistema
		@SuppressWarnings("rawtypes")
		ResponseEntity<ResponseApi> authenticationResponse = restTemplate.exchange(lp.getRolesRestApi(),HttpMethod.GET, new HttpEntity<Object>(headers), ResponseApi.class);
		if (authenticationResponse.getBody().getStatus().equals(String.valueOf(HttpStatus.OK.value()))) {
			// Iniciamos la lectura
			@SuppressWarnings("unchecked")
			ArrayList<String> listado = (ArrayList<String>) authenticationResponse.getBody().getData();
			Collection<RolDTO> roles = HelperRest.getObjectToJsonMasivo(listado);
			return roles;
		} else {
			logger.warn("No se han encontrado roles configurados en la aplicacion");
			throw new Exception(authenticationResponse.getBody().getStatus() + authenticationResponse.getBody().getMsg());
		}
	}
}