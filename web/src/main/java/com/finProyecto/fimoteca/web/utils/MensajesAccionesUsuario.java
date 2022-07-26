package com.finProyecto.fimoteca.web.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MensajesAccionesUsuario {

	private final String mensajeError = "error";
	private final String mensajeAdvertencia = "advertencia";
	private final String mensajeCorrectos = "correctos";

	private Map<String, Collection<String>> mensajesUsuario = new HashMap<>();

	public MensajesAccionesUsuario() {
		mensajesUsuario.put(mensajeAdvertencia, new ArrayList<>());
		mensajesUsuario.put(mensajeError, new ArrayList<>());
		mensajesUsuario.put(mensajeCorrectos, new ArrayList<>());
	}

	public void agregarMensajeError(String mensaje) {
		mensajesUsuario.get(mensajeError).add(mensaje);
	}

	public void agregarMensajeAdvertencia(String mensaje) {
		mensajesUsuario.get(mensajeAdvertencia).add(mensaje);
	}

	public void agregarMensajeCorrecto(String mensaje) {
		mensajesUsuario.get(mensajeCorrectos).add(mensaje);
	}

	public Map<String, Collection<String>> getMensajes() {
		return mensajesUsuario;
	}
}
