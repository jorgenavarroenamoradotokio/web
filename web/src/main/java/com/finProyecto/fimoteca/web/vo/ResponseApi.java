package com.finProyecto.fimoteca.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseApi<T> {

	private String status;
	private String msg;
	private T data;

}
