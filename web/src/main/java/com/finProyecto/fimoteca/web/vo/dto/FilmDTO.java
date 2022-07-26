package com.finProyecto.fimoteca.web.vo.dto;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilmDTO {

	private long id;
	private String title;
	private Integer year;
	private Integer duration;
	private String sypnosis;
	private String image;
	private Collection<PersonDTO> person;
}