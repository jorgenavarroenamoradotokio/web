package com.finProyecto.fimoteca.web.controller.person;

import java.util.Arrays;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.finProyecto.fimoteca.web.negocio.exception.DuplicateException;
import com.finProyecto.fimoteca.web.negocio.person.IPersonService;
import com.finProyecto.fimoteca.web.utils.Constantes;
import com.finProyecto.fimoteca.web.utils.LectorPropiedades;
import com.finProyecto.fimoteca.web.utils.Utilidades;
import com.finProyecto.fimoteca.web.vo.TypePersonEnum;
import com.finProyecto.fimoteca.web.vo.dto.PersonDTO;
import com.finProyecto.fimoteca.web.vo.formularios.PersonForm;

@Controller
public class PersonController {

	private final static Logger logger = LoggerFactory.getLogger(PersonController.class);

	@Autowired
	IPersonService service;

	@Autowired
	LectorPropiedades lp;
	
	@Autowired
	Utilidades utils;

	/**
	 * 
	 * Visualizamos la pantalla de alta de personas con todos los datos necesarios
	 * para que el usuario pueda interacturar con ella
	 * 
	 * @param model
	 * @return String view
	 */

	@GetMapping("/new-person")
	public String preNewPerson(Model model) throws Exception {
		logger.info("Iniciamos presentacion de la pantalla de alta de personas");
		String view = lp.getViewIndex();

		model.addAttribute("listadoTiposPersona", Arrays.asList(TypePersonEnum.values()));
		model.addAttribute("person", new PersonForm());
		model.addAttribute("plantilla", lp.getViewNewPerson());
		return view;
	}
	
	/**
	 * 
	 * Iniciamos el proceso de registro de una persona.
	 * Validamos que los campos obligatorios vengan rellenos en caso de no ser asi notificamos al usuario el error
	 * Validamos que la persona insertada no este registrado previamente en nuestros sistemas
	 * 
	 * @param form
	 * @param result
	 * @param model
	 * @return
	 */
	
	@PostMapping("/new-person")
	public String altaPersona(@Valid @ModelAttribute("person") PersonForm form, BindingResult result, Model model) throws Exception {
		logger.info("Inciamos el proceso de registro de una persona");
		String view = lp.getViewIndex();
		if (result.hasErrors()) {
			// Notificamos que se han producido errores a la hora de iniciar sesion
			logger.debug("Se han encontrado " + result.getErrorCount() + " errores de validacion");
		} else {
			PersonDTO personDTO = PersonDTO.builder().name(form.getName()).surname(form.getSurname()).typePerson(TypePersonEnum.getInstance(Long.parseLong(form.getTypePerson()))).build();
			String[] param = { personDTO.getName(), personDTO.getSurname() };
			try {
				service.registrarPersona(personDTO);
				utils.getMensajes().agregarMensajeCorrecto(utils.leerMensajeConParametro(Constantes.PROPIEDAD_MSG_PERSONA_REGISTRADA_EXITO, param));
				model.addAttribute("person", new PersonForm());
			} catch (DuplicateException ex) {
				logger.info("Se ha intendado registrar a una persona ya registrada");
				utils.getMensajes().agregarMensajeError(utils.leerMensajeConParametro(Constantes.PROPIEDAD_MSG_PERSONA_DUPLICADA, param));
			}
		}
		
		model.addAttribute("listadoTiposPersona", Arrays.asList(TypePersonEnum.values()));
		model.addAttribute("plantilla", lp.getViewNewPerson());
		model.addAttribute("msgAcciones", utils.getMensajes());
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
}