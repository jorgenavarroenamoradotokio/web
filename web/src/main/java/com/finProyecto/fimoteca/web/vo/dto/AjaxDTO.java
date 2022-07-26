package com.finProyecto.fimoteca.web.vo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AjaxDTO<T> {

	private String status;
	private T data;

}
