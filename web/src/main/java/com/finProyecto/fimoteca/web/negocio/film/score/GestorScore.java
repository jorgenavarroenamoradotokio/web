package com.finProyecto.fimoteca.web.negocio.film.score;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finProyecto.fimoteca.web.negocio.person.GestorPerson;
import com.finProyecto.fimoteca.web.persistencia.film.score.IScoreRepository;
import com.finProyecto.fimoteca.web.persistencia.film.score.Score;
import com.finProyecto.fimoteca.web.utils.Utilidades;
import com.finProyecto.fimoteca.web.vo.dto.ScoreDTO;

@Service
public class GestorScore implements IScoreService {

	private final static Logger logger = LoggerFactory.getLogger(GestorPerson.class);
	
	@Autowired
	Utilidades utils;
	
	@Autowired
	IScoreRepository repository;
	
	/**
	 * 
	 * Obtenemos la media de puntuacion que pose una pelicula
	 * 
	 */
	
	@Override
	public double obtenerMediaPelicula(long idPelicula) {
		logger.info("Iniciamos el calculo de media de puntuacion de una pelicula");
		Double avg = repository.avgScoreFilm(idPelicula);
		if (avg == null) {
			return 0;
		}
		return avg;
	}

	/**
	 * 
	 * Obtenemos la puntuacion que ha realizado un usuario sobre una pelicula
	 * 
	 */
	
	@Override
	public ScoreDTO obtenerInformacionPuntuacionAsociadaUsuario(long idUser, long idFilm) {
		logger.info("Obtenemos la informacion completa de una puntuacion de una peli asociada a un usuario");
		Score score = repository.existeScorePorFilmUser(idUser, idFilm).orElse(null);
		if(score != null) {
			return utils.mapperScore(score);	
		}
		return null;
	}

	/**
	 * 
	 * Registramos una puntuacion
	 * 
	 */
	@Override
	public void registrarPuntuacion(Score score) {
		repository.save(score);
	}
}
