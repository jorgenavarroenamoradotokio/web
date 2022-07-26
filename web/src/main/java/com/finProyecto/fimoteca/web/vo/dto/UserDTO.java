package com.finProyecto.fimoteca.web.vo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {

	private String username;
	private String password;
	private String name;
	private String surname;
	private String email;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "Europe/Madrid")
	private Date birthDate;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDate creationDate;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime lastLogin;
	private boolean active;
	private RolDTO rol;
}