package com.finProyecto.fimoteca.web.persistencia.film.score;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.finProyecto.fimoteca.web.persistencia.film.Film;
import com.finProyecto.fimoteca.web.utils.Constantes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Constantes.TABLA_PUNTUACIONES)
public class Score {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private double points;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_film", nullable = false, foreignKey = @ForeignKey(name = "FK_SCORE_FILM_ID", foreignKeyDefinition = "FOREIGN KEY (id_film) REFERENCES " + Constantes.TABLA_PELICULA + "(id) ON DELETE CASCADE"))
	private Film film;

	private long idUser;
}