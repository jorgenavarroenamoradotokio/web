package com.finProyecto.fimoteca.web.persistencia.film;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(JUnit4.class)
class TestRepositoryFilm {

	@Autowired
	IFilmRepository repository;
	
	@Test
	public void notFoudFilmByTitle() {
		Optional<Film> opt = repository.findByTitle("ASD");
		assertTrue(opt.isEmpty());
	}
	
	@Test
	public void foundFilmByTitle() {
		Film f = repository.save(Film.builder().title("t").duration(0).year(0).sypnosis("asd").build());
		Optional<Film> opt = repository.findByTitle(f.getTitle());
		assertTrue(opt.isPresent());
		repository.delete(f);
	}
}
