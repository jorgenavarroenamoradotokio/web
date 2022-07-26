package com.finProyecto.fimoteca.web.vo.formularios;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonForm {

	@NotNull
	private String name;
	
	@NotNull
	private String surname;
	
	@NotNull
	private String typePerson;
}
