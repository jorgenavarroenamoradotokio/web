package com.finProyecto.fimoteca.web.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.finProyecto.fimoteca.web.utils.LectorPropiedades;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	LectorPropiedades lp;
	
	/**
	 * 
	 * Definimos la siguiente configuracion.
	 * csrf deshabilitado, nos los proporciona jwt
	 * contexto raiz puede acceder todo el mundo
	 * login puede acceder todo el mundo
	 * new-user(registro de usuario) puede acceder todo el mundo
	 * definimos la accion de logout
	 * definimos las acciones de login
	 * 
	 */
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/login").permitAll()
		.antMatchers("/new-user").permitAll()
		.antMatchers("/new-person").permitAll()
		.antMatchers("/new-film").permitAll()
		.antMatchers("/search").permitAll()
		.antMatchers("/ajax/**").permitAll()
		.antMatchers("/view-film/**").permitAll()
		.anyRequest().authenticated()
		.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/?message=logout");
	}

	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/templates/**", "/css/**", "/js/**", "/images/**",
				"/fonts/**", "/error", "/webjars/**");
	}
}
