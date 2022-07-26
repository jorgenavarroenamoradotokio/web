package com.finProyecto.fimoteca.web.persistencia.film;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IFilmRepository extends CrudRepository<Film, Long> {

	Optional<Film> findByTitle(@Param("title") String title);

	Set<Film> findByTitleStartsWith(@Param("title") String title);

	@Query("SELECT f FROM Film f WHERE f.title LIKE %:title%")
	Set<Film> searchFilmByWord(@Param("title") String title);
}
