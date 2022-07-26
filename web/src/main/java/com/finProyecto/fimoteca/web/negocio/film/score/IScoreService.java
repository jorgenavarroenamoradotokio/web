package com.finProyecto.fimoteca.web.negocio.film.score;

import com.finProyecto.fimoteca.web.persistencia.film.score.Score;
import com.finProyecto.fimoteca.web.vo.dto.ScoreDTO;

public interface IScoreService {

	double obtenerMediaPelicula(long idPelicula);
	
	ScoreDTO obtenerInformacionPuntuacionAsociadaUsuario(long idUser, long idFilm);
	
	void registrarPuntuacion(Score score);

}
