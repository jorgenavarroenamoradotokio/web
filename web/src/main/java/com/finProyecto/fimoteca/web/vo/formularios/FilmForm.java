package com.finProyecto.fimoteca.web.vo.formularios;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilmForm {

	@NotNull
	private String title;

	@NotNull
	private Integer year;

	@NotNull
	private Integer duration;

	@NotNull
	private String sypnosis;

	private String director;
	private Collection<String> guionistas;
	private Collection<String> actores;
	private Collection<String> musicos;
	private String fotografo;

}
