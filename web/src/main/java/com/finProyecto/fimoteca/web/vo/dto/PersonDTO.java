package com.finProyecto.fimoteca.web.vo.dto;

import com.finProyecto.fimoteca.web.vo.TypePersonEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonDTO {
	private long id;
	private String name;
	private String surname;
	private TypePersonEnum typePerson;
}