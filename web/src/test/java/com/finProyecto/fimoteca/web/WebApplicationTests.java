package com.finProyecto.fimoteca.web;

import java.time.LocalDateTime;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finProyecto.fimoteca.web.utils.HelperRest;
import com.finProyecto.fimoteca.web.vo.dto.UserDTO;

@SpringBootTest
class WebApplicationTests {

	@Test
	void contextLoads() {
		try {
			String json = HelperRest.getBodyUser(UserDTO.builder().birthDate(new Date()).lastLogin(LocalDateTime.now()).build());
			System.out.println(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
