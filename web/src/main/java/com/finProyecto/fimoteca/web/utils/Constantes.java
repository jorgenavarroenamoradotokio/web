package com.finProyecto.fimoteca.web.utils;

public class Constantes {

	// Contante de de configuracion para los perfiles
	public static final String PERFIL_DEFECTO_APP = "perfil.defecto.app";
	public static final String PERFIL_ADMIN_APP = "perfil.admin.app";

	// Constante de configuracion del rest api
	public static final long DURATION_MILISECOND = 3000;
	public static final String CLAVE_TOKE_SESION = "bearerToken";
	
	// Constantes de sesion
	public static final String USERNAME_SESION = "username";
	public static final String ROL_USER_SESION = "rolUsername";
	public static final String ID_USER_CONECTADO = "idUserConectado";
	
	// Constantes de abecedario
	public static final String[] ABECEDDARIO = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	
	// Constantes tablas base de datos
	public static final String TABLA_PERSONA = "persons";
	public static final String TABLA_PELICULA="films";
	public static final String TABLA_PERSONA_PELICULA = "rel_person_film";
	public static final String TABLA_PUNTUACIONES = "scores";
	public static final String TABLA_USUARIO = "users";
	
	// Constantes profesiones personas
	public static final String PROPIEDAD_VALOR_GUIONISTA = "texto_guionista";
	public static final String PROPIEDAD_VALOR_MUSICO = "texto_musico";
	public static final String PROPIEDAD_VALOR_FOTOGRAFO = "texto_fotografo";
	public static final String PROPIEDAD_VALOR_ACTOR = "texto_actor";
	public static final String PROPIEDAD_VALOR_DIRECTOR = "texto_director";

	// Constantes de configuracion URL rest api
	protected static final String PROPIEDAD_LOGIN_API_REST = "url.restapi.login";
	protected static final String PROPIEDAD_SING_IN_API_REST = "url.restapi.singin";
	protected static final String PROPIEDAD_ROLES_API_REST = "url.restapi.roles";
	protected static final String PROPIEDAD_ROL_USUARIO_CONECTADO_API_REST = "url.restapi.rol.usuario.conectado";
	protected static final String PROPIEDAD_ID_USUARIO_CONECTADO_API_REST = "url.restapi.id.usuario.conectado";
	protected static final String PROPIEDAD_ACTUALIZAR_HORA_LOGIN_API_REST = "url.restapi.usuario.lastlogin";
	protected static final String PROPIEDAD_CONSULTAR_OPINIONES_API_REST = "url.restapi.review.consultar";
	protected static final String PROPIEDAD_REGISTRAR_OPINION_API_REST = "url.restapi.review.registrar";

	// Constantes de configuracion URL de las vistas
	protected static final String PROPIEDAD_VIEW_INDEX = "url.view.index";
	protected static final String PROPIEDAD_VIEW_LOGIN = "url.view.user.login";
	protected static final String PROPIEDAD_VIEW_SIGN_IN = "url.view.user.registro";
	protected static final String PROPIEDAD_VIEW_ERROR = "url.view.error";
	protected static final String PROPIEDAD_VIEW_PERSON = "url.view.person";
	protected static final String PROPIEDAD_VIEW_FILM_SEARCH = "url.view.film.search";
	protected static final String PROPIEDAD_VIEW_FILM = "url.view.film.alta";
	protected static final String PROPIEDAD_VIEW_FILM_CONSULTING = "url.view.film.view";
	
	// Constantes de configuracion de textos de idiomas
	public static final String PROPIEDAD_MSG_USUARIO_PASSWORD_ERRONEOS = "mensaje_error_login_usuario_password";
	public static final String PROPIEDAD_MSG_USUARIO_DUPLICADO = "mensaje_error_registro_usuario_duplicado";
	public static final String PROPIEDAD_MSG_USUARIO_REGISTRADO_EXITO = "mensaje_registro_usuario_correcto";
	public static final String PROPIEDAD_MSG_PERSONA_DUPLICADA = "mensaje_error_registro_persona_nombre_tipo_duplicado";
	public static final String PROPIEDAD_MSG_PERSONA_REGISTRADA_EXITO = "mensaje_registro_persona_correcto";
	public static final String PROPIEDAD_MSG_PELICULA_DUPLICADA = "mensaje_registro_film_duplicado";
	public static final String PROPIEDAD_MSG_PELICULA_REGISTRADA_EXITO = "mensaje_registro_film_correcta";
	public static final String PROPIEDAD_MSG_PELICULA_IMAGEN_ERROR = "mensaje_registro_film_imagen_error";
	public static final String PROPIEDAD_MSG_PELICULA_NO_ENCONTRADA = "mensaje_busqueda_film_sin_datos";
	public static final String PROPIEDAD_MSG_PELICULA_CONSULTAR_ERROR = "mensaje_error_consultar_fim";
	public static final String PROPIEDAD_MSG_AJAX_COMENTARIO_DUPLICADO = "mensaje_duplicado_review_insertada";
	public static final String PROPIEDAD_MSG_AJAX_PUNTUACION_DUPLICADO = "mensaje_duplicado_puntuacion_insertada";
	public static final String PROPIEDAD_MSG_AJAX_COMENTARIO_CRITERIOS_NO_CUMPLIDOS = "mensaje_error_criterios_nom_cumplidos_review";
	
	// Constantes deconfiguracion subida de documentos
	public static final String PROPIEDAD_DIRECTORIO_CONFIGURACION_DOCUMENTOS = "images.upload.dir";
	public static final String PROPIEDAD_DIRECTORIO_SUBIDA_DOCUMENTOS = "images.server.dir";

}

