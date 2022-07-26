package com.finProyecto.fimoteca.web.persistencia.film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.finProyecto.fimoteca.web.persistencia.person.Person;
import com.finProyecto.fimoteca.web.utils.Constantes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = Constantes.TABLA_PELICULA)
public class Film {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String title;
	private int year;
	private int duration;
	@Lob
	private String sypnosis;
	private String poster;
	private boolean migrate;
	private LocalDate dateMigrate;

	@JoinTable(name = Constantes.TABLA_PERSONA_PELICULA, joinColumns = @JoinColumn(name = "FK_FILM", nullable = false, foreignKey = @ForeignKey(name = "FK_FILM_ID", foreignKeyDefinition = "FOREIGN KEY (FK_FILM) REFERENCES "
			+ Constantes.TABLA_PELICULA + "(id) ON DELETE CASCADE")), inverseJoinColumns = @JoinColumn(name = "FK_PERSON", nullable = false, foreignKey = @ForeignKey(name = "FK_PERSON_ID", foreignKeyDefinition = "FOREIGN KEY (FK_PERSON) REFERENCES "
			+ Constantes.TABLA_PERSONA + "(id) ON DELETE CASCADE")))
	@ManyToMany
	private List<Person> persons;

	public void addPerson(Person person) {
		if (this.persons == null) {
			this.persons = new ArrayList<>();
		}
		this.persons.add(person);
	}
}
