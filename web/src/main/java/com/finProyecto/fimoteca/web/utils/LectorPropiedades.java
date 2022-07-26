package com.finProyecto.fimoteca.web.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class LectorPropiedades {

	@Autowired
	private Environment env;

	public String getValorPropiedad(String valorPropiedad) {
		return env.getProperty(valorPropiedad);
	}
	
	public String getLoginRestApi() {
		return getValorPropiedad(Constantes.PROPIEDAD_LOGIN_API_REST);
	}
	
	public String getSingInRestApi() {
		return getValorPropiedad(Constantes.PROPIEDAD_SING_IN_API_REST);
	}
	
	public String getRolesRestApi() {
		return getValorPropiedad(Constantes.PROPIEDAD_ROLES_API_REST);
	}
	
	public String getRolUsuarioConectadoApi() {
		return getValorPropiedad(Constantes.PROPIEDAD_ROL_USUARIO_CONECTADO_API_REST);
	}
	
	public String getIdUsuarioConectadoApi() {
		return getValorPropiedad(Constantes.PROPIEDAD_ID_USUARIO_CONECTADO_API_REST);
	}
	
	public String getActualizarLastLoginApi() {
		return getValorPropiedad(Constantes.PROPIEDAD_ACTUALIZAR_HORA_LOGIN_API_REST);
	}
	
	public String getConsultarOpionesPeliculaApi() {
		return getValorPropiedad(Constantes.PROPIEDAD_CONSULTAR_OPINIONES_API_REST);
	}
	
	public String getRegistrarOpionesPeliculaApi() {
		return getValorPropiedad(Constantes.PROPIEDAD_REGISTRAR_OPINION_API_REST);
	}
	
	public String getViewIndex() {
		return getValorPropiedad(Constantes.PROPIEDAD_VIEW_INDEX);
	}
	
	public String getViewLogin() {
		return getValorPropiedad(Constantes.PROPIEDAD_VIEW_LOGIN);
	}
	
	public String getViewSingIn() {
		return getValorPropiedad(Constantes.PROPIEDAD_VIEW_SIGN_IN);
	}
	
	public String getViewNewPerson() {
		return getValorPropiedad(Constantes.PROPIEDAD_VIEW_PERSON);
	}
	
	public String getViewFilmSearch() {
		return getValorPropiedad(Constantes.PROPIEDAD_VIEW_FILM_SEARCH);
	}
	
	public String getViewNewFilm() {
		return getValorPropiedad(Constantes.PROPIEDAD_VIEW_FILM);
	}
	
	public String getViewConsultingFilm() {
		return getValorPropiedad(Constantes.PROPIEDAD_VIEW_FILM_CONSULTING);
	}
}
