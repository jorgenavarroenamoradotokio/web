package com.finProyecto.fimoteca.web.vo;

import java.util.Arrays;

import com.finProyecto.fimoteca.web.utils.Constantes;

public enum TypePersonEnum {

	GUIONISTA(1, Constantes.PROPIEDAD_VALOR_GUIONISTA), MUSICO(2, Constantes.PROPIEDAD_VALOR_MUSICO),
	FOTOGRAFO(3, Constantes.PROPIEDAD_VALOR_FOTOGRAFO), ACTOR(4, Constantes.PROPIEDAD_VALOR_ACTOR),
	DIRECTOR(5, Constantes.PROPIEDAD_VALOR_DIRECTOR);
	
	private long id;
	private String valor;

	TypePersonEnum(long id, String valor) {
		this.id = id;
		this.valor = valor;
	}

	public long getId() {
		return id;
	}

	public String getValor() {
		return valor;
	}

	public static TypePersonEnum getInstance(long id) {
		TypePersonEnum t = Arrays.asList(TypePersonEnum.values()).stream().filter(element -> element.getId()== id).findFirst().get();
		return t;
	}
}
