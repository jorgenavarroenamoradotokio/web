package com.finProyecto.fimoteca.web.utils;

import java.util.ArrayList;
import java.util.Collection;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.finProyecto.fimoteca.web.persistencia.film.Film;
import com.finProyecto.fimoteca.web.persistencia.film.score.Score;
import com.finProyecto.fimoteca.web.persistencia.person.Person;
import com.finProyecto.fimoteca.web.vo.dto.FilmDTO;
import com.finProyecto.fimoteca.web.vo.dto.PersonDTO;
import com.finProyecto.fimoteca.web.vo.dto.ScoreDTO;
import com.finProyecto.fimoteca.web.vo.dto.UserDTO;
import com.finProyecto.fimoteca.web.vo.formularios.FilmForm;
import com.finProyecto.fimoteca.web.vo.formularios.PersonForm;
import com.finProyecto.fimoteca.web.vo.formularios.SignInForm;

public class Utilidades {

	@Autowired
	ModelMapper mapper;
	
	@Autowired
	MensajesAccionesUsuario msg;
	
	@Autowired
	MessageSource messages;
	
	/**
	 * 
	 * Enviamos la informacion del formulario SignInForm a un objeto UserDTO
	 * 
	 * @param form
	 * @return
	 */
	
	public UserDTO mapperUser(SignInForm form) {
		mapper.typeMap(SignInForm.class, UserDTO.class).addMappings(mapper -> mapper.skip(UserDTO::setRol));
		UserDTO user = mapper.map(form, UserDTO.class);
		return user;
	}
	
	/**
	 * 
	 * Enviamos la informacion del formulario PersonForm a un objeto PersonDTO
	 * 
	 * @param form
	 * @return
	 */
	
	public PersonDTO mapperPerson(PersonForm form) {
		mapper.typeMap(PersonForm.class, PersonDTO.class).addMappings(mapper -> mapper.skip(PersonDTO::setTypePerson));
		PersonDTO person = mapper.map(form, PersonDTO.class);
		return person;
	}
	
	/**
	 * 
	 * Enviamos la informacion del PersonDTO a un objeto Person
	 * 
	 * @param personDTO
	 * @return
	 */
	
	public Person mapperPerson(PersonDTO personDTO) {
		mapper.typeMap(PersonDTO.class, Person.class).addMappings(mapper -> mapper.skip(Person::setFilms));
		Person person = mapper.map(personDTO, Person.class);
		return person;
	}
	
	
	/**
	 * 
	 * Enviamos la informacion del Person a un objeto PersonDTO
	 * 
	 * @param personDTO
	 * @return
	 */
	
	public PersonDTO mapperPerson(Person person) {
		PersonDTO personDTO = mapper.map(person, PersonDTO.class);
		return personDTO;
	}
	
	/**
	 * 
	 * Enviamos la informacion del FilmForm a un objeto FilmDTO
	 * 
	 * @param personDTO
	 * @return
	 */
	
	public FilmDTO mapperFilm(FilmForm form) {
		mapper.typeMap(FilmForm.class, FilmDTO.class).addMappings(mapper-> mapper.skip(FilmDTO::setPerson));
		FilmDTO film = mapper.map(form, FilmDTO.class);
		Collection<PersonDTO> person = new ArrayList<>();		
		
		if (form.getDirector() != null) {
			person.add(PersonDTO.builder().id(Long.parseLong(form.getDirector())).build());
		}

		if (form.getActores() != null) {
			form.getActores().forEach(id -> person.add(PersonDTO.builder().id(Long.parseLong(id)).build()));
		}

		if (form.getGuionistas() != null) {
			form.getGuionistas().forEach(id -> person.add(PersonDTO.builder().id(Long.parseLong(id)).build()));
		}

		if (form.getFotografo() != null) {
			person.add(PersonDTO.builder().id(Long.parseLong(form.getFotografo())).build());
		}

		if (form.getMusicos() != null) {
			form.getMusicos().forEach(id -> person.add(PersonDTO.builder().id(Long.parseLong(id)).build()));
		}
		
		film.setPerson(person);
		
		return film;
	}
	
	/**
	 * 
	 * Enviamos la informacion del FilmDTO a un objeto Film
	 * 
	 * @param filmDTO
	 * @return
	 */
	
	public Film mapperFilm(FilmDTO filmDTO) {
		mapper.typeMap(FilmDTO.class, Film.class).addMappings(mapper-> mapper.skip(Film::setPersons));
		mapper.typeMap(FilmDTO.class, Film.class).addMappings(mapper-> mapper.skip(Film::setPoster));
		Film film = mapper.map(filmDTO, Film.class);
		if(filmDTO.getPerson() != null) {
			filmDTO.getPerson().forEach(personDTO -> film.addPerson(mapperPerson(personDTO)));
		}
		film.setPoster(filmDTO.getImage());
		return film;
	}
	
	/**
	 * 
	 * Enviamos la informacion del Film a un objeto FilmDTO
	 * 
	 * @param filmDTO
	 * @return
	 */
	
	public FilmDTO mapperFilm(Film film) {
		mapper.typeMap(Film.class, FilmDTO.class).addMappings(mapper-> mapper.skip(FilmDTO::setPerson));
		mapper.typeMap(Film.class, FilmDTO.class).addMappings(mapper-> mapper.skip(FilmDTO::setImage));
		FilmDTO filmDTO = mapper.map(film, FilmDTO.class);
		Collection<PersonDTO> listPerson = new ArrayList<>();
		if (film.getPersons() != null) {
			film.getPersons().forEach(person -> listPerson.add(mapperPerson(person)));
		}
		
		filmDTO.setImage(film.getPoster());
		filmDTO.setPerson(listPerson);
		return filmDTO;
	}
	
//	public Score mapperScore(ScoreDTO scoreDTO) {
//		mapper.typeMap(ScoreDTO.class, Score.class).addMappings(mapper-> mapper.skip(Score::setFilm));
//		mapper.typeMap(ScoreDTO.class, Score.class).addMappings(mapper-> mapper.skip(Score::setIdUser));
//		Score score = mapper.map(scoreDTO, Score.class);
//		score.setFilm(Film.builder().id(scoreDTO.getFilmID()).build());
//		score.setIdUser(scoreDTO.getUserID());
//		return score;
//	}
	
	public Score mapperScore(ScoreDTO scoreDTO) {
		mapper.typeMap(ScoreDTO.class, Score.class).addMappings(mapper-> mapper.skip(Score::setFilm));
		mapper.typeMap(ScoreDTO.class, Score.class).addMappings(mapper-> mapper.skip(Score::setIdUser));
		Score score = mapper.map(scoreDTO, Score.class);
		score.setFilm(Film.builder().id(scoreDTO.getFilmID()).build());
		score.setIdUser(scoreDTO.getUserID());
		return score;
	}
	
	
	public ScoreDTO mapperScore(Score score) {
		mapper.typeMap(Score.class, ScoreDTO.class).addMappings(mapper-> mapper.skip(ScoreDTO::setFilmID));
		mapper.typeMap(Score.class, ScoreDTO.class).addMappings(mapper-> mapper.skip(ScoreDTO::setUserID));
		ScoreDTO scoreDTO = mapper.map(score, ScoreDTO.class);
		scoreDTO.setFilmID(score.getFilm().getId());
		scoreDTO.setUserID(score.getIdUser());
		return scoreDTO;
	}
	
	/**
	 * 
	 * Obtenemos todas las funciones necesarias para registrar los mensajes de
	 * error, avisos y de acciones correctas
	 * 
	 * @return
	 */
	
	public MensajesAccionesUsuario getMensajes() {
		return msg;
	}

	/**
	 * 
	 * Leemos los textos definidos en el documento de idiomas sin parametros
	 * 
	 * @param clave
	 * @return
	 */
	
	public String leerMensajeSinParametro(String clave) {
		return leerMensajeConParametro(clave, null);
	}
	
	/**
	 * 
	 * Leemos los textos definidos en el documento de idiomas con parametros
	 * 
	 * @param clave
	 * @param parametro
	 * @return
	 */

	public String leerMensajeConParametro(String clave, String[] parametro) {
		return messages.getMessage(clave, parametro, LocaleContextHolder.getLocale());
	}
}
