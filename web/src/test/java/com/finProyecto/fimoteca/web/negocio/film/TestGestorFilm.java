package com.finProyecto.fimoteca.web.negocio.film;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.finProyecto.fimoteca.web.negocio.exception.DuplicateException;
import com.finProyecto.fimoteca.web.persistencia.film.Film;
import com.finProyecto.fimoteca.web.persistencia.film.IFilmRepository;
import com.finProyecto.fimoteca.web.utils.Utilidades;
import com.finProyecto.fimoteca.web.vo.dto.FilmDTO;

@SpringBootTest
@RunWith(JUnit4.class)
class TestGestorFilm {

	@Mock
	IFilmRepository repositoryMock;
	
	@Autowired
	Utilidades utils;

	@Test()
	public void personDuplicateInsert() {
		Film f = Film.builder().title("asd").build();
		Optional<Film> opt = Optional.of(f);

		when(repositoryMock.findByTitle(Mockito.anyString())).thenReturn(opt);
		
		GestorFilm gestor = new GestorFilm();
		gestor.utils = utils;
		gestor.repository = repositoryMock;

		DuplicateException thrown = assertThrows(DuplicateException.class, () -> {
			gestor.registrarPelicula(FilmDTO.builder().title("asd").build());
		}, "Error");

		assertNotNull(thrown);
	}

}
