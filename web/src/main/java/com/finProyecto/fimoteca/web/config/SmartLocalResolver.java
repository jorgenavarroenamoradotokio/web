package com.finProyecto.fimoteca.web.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

public class SmartLocalResolver extends AcceptHeaderLocaleResolver {

	// Listado con los idiomas por defecto que trata nuestra web
	List<Locale> locales = Arrays.asList(new Locale("es"));

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		// Obtenemos el lengauaje de la cabecera
		if (StringUtils.isBlank(request.getHeader("Accept-Langauge"))) {
			return Locale.getDefault();
		}

		// Comparamos el idioma que se ha detectado en la request con los que tenemos configurados en nuestro sistema
		List<Locale.LanguageRange> list = Locale.LanguageRange.parse(request.getHeader("Accept-Langauge"));
		Locale locale = Locale.lookup(list, locales);
		return locale;
	}
}
