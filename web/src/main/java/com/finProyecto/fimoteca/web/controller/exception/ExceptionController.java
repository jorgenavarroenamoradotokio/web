package com.finProyecto.fimoteca.web.controller.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.finProyecto.fimoteca.web.utils.LectorPropiedades;

@ControllerAdvice
public class ExceptionController {

	private final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

	@Autowired
	LectorPropiedades lectorPropiedades;

	/**
	 * 
	 * Tratamiento de excepciones de manera generica y mostrando por pantalla dicha informacion
	 * 
	 * @param request
	 * @param exception
	 * @return
	 */
	
	@ExceptionHandler
	public ModelAndView hadlerException(HttpServletRequest request, Exception exception) {
		logger.error("Error no controlado", exception);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mensaje", exception.getMessage()); // mensaje de error
		modelAndView.addObject("exception", exception); // excepcion que se ha producido
		modelAndView.addObject("url", request.getRequestURL());// url de orign del error
		modelAndView.setViewName("error");
		return modelAndView;
	}
}
