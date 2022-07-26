package com.finProyecto.fimoteca.web.persistencia.film.score;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IScoreRepository extends CrudRepository<Score, Long> {

	@Query("SELECT AVG(s.points) FROM Score s where s.film.id = :idFilm")
	public Double avgScoreFilm(@Param("idFilm") long idFilm);
	
	@Query("SELECT s FROM Score s where s.film.id = :idFilm and s.idUser = :idUser")
	public Optional<Score> existeScorePorFilmUser(@Param("idUser") long idUser, @Param("idFilm") long idFilm);
}
