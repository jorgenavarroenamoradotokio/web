package com.finProyecto.fimoteca.web.vo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDTO {

	private long id;
	private long points;
	private long userID;
	private long filmID;

}
