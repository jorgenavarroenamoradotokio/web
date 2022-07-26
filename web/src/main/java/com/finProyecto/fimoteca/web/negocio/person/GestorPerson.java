package com.finProyecto.fimoteca.web.negocio.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finProyecto.fimoteca.web.negocio.exception.DuplicateException;
import com.finProyecto.fimoteca.web.persistencia.person.IPersonRepository;
import com.finProyecto.fimoteca.web.persistencia.person.Person;
import com.finProyecto.fimoteca.web.utils.Utilidades;
import com.finProyecto.fimoteca.web.vo.dto.PersonDTO;

@Service
public class GestorPerson implements IPersonService {

	private final static Logger logger = LoggerFactory.getLogger(GestorPerson.class);

	@Autowired
	Utilidades utils;

	@Autowired
	IPersonRepository repositorio;

	/**
	 * 
	 * Iniciamos la busqueda masiva de informacion de personas a partir de un
	 * listado idPersonas. Como resultado retornamos la informacion en un objeto
	 * PersonDTO
	 * 
	 */

	@Override
	public Set<PersonDTO> buscarPersonMasivoPorId(Collection<Long> id) {
		logger.info("Iniciamos la busqueda de informacion de una persona de manera masiva");
		Set<PersonDTO> listPersonDTO = new HashSet<>();
		if (id != null && !id.isEmpty()) {
			Set<Person> listPerson = repositorio.buscarPersonaPorIdMasivo(id).orElse(new HashSet<Person>());
			if (!listPerson.isEmpty()) {
				listPerson.forEach(nodo -> listPersonDTO.add(utils.mapperPerson(nodo)));
			}
		}

		return listPersonDTO;
	}

	/**
	 * 
	 * Iniciamos la busqueda individual de informacion de personas a partir de un
	 * idPersonas. Como resultado retornamos la informacion en un objeto PersonDTO
	 * 
	 */

	@Override
	public PersonDTO buscarPersonPorId(long id) {
		logger.info("Iniciamos la busqueda de informacion de una persona por id");
		Set<Long> ids = new HashSet<>();
		ids.add(id);
		return new ArrayList<>(buscarPersonMasivoPorId(ids)).get(0);
	}

	
	/**
	 * 
	 * Obtenemos todas las personas registradas en nuestros sistemas agrupadas por
	 * el tipo de persona que tiene
	 * 
	 */
	
	@Override
	public Map<Long, Collection<PersonDTO>> obtenerTodasPersonasAgrupadasPorTipoPersona() {
		logger.info("Iniciamos el proceso de agrupar las personas por tipos de persona");
		Iterable<Person> it = repositorio.findAll();
		Map<Long, Collection<PersonDTO>> mapPersonDTO = new HashMap<>();
		if (it != null) {
			Collection<PersonDTO> listPerson = new ArrayList<>();
			it.iterator().forEachRemaining(person -> listPerson.add(utils.mapperPerson(person)));
			for (PersonDTO personDTO : listPerson) {
				Collection<PersonDTO> aux = mapPersonDTO.get(personDTO.getTypePerson().getId());
				if (aux == null) {
					aux = new ArrayList<PersonDTO>();
					mapPersonDTO.put(personDTO.getTypePerson().getId(), aux);
				}
				aux.add(personDTO);
			}
		}
		return mapPersonDTO;
	}

	/**
	 * 
	 * Verificamos existencia de persona para evitar duplicidad
	 * En caso de existir lanzamos {@link DuplicateException}
	 * En caso de que no exista registramos la persona en nuestros sistemas
	 * 
	 */
	
	@Override
	public boolean registrarPersona(PersonDTO personDTO) throws DuplicateException {
		boolean existe = repositorio.finByNameAndSurnameAndTypePerson(personDTO.getName(), personDTO.getSurname(), personDTO.getTypePerson()).orElse(null) != null;
		if (existe) {
			throw new DuplicateException();
		}
		return (Person)repositorio.save(utils.mapperPerson(personDTO)) != null; 
	}
}
