package com.finProyecto.fimoteca.web.vo.formularios;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignInForm extends LoginForm {

	@NotNull
	private String name;

	@NotNull
	private String surname;

	@NotNull
	@Email(regexp = ".+[@].+[\\.].+")
	private String email;

	@NotNull
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private String birthDate;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDate creationDate;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime lastLogin;

	private boolean active;

	private String rol;
}
