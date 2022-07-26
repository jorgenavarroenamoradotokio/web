package com.finProyecto.fimoteca.web.persistencia.person;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.finProyecto.fimoteca.web.persistencia.film.Film;
import com.finProyecto.fimoteca.web.utils.Constantes;
import com.finProyecto.fimoteca.web.vo.TypePersonEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = Constantes.TABLA_PERSONA)
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String surname;

	@Enumerated(EnumType.STRING)
	@Column(name = "type_person")
	private TypePersonEnum typePerson;

	@ManyToMany(mappedBy = "persons")
	private List<Film> films;

}