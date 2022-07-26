package com.finProyecto.fimoteca.web.vo.formularios;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginForm {

	@NotNull
	private String username;

	@NotNull
	private String password;

}
