package com.finProyecto.fimoteca.web.negocio.film;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.finProyecto.fimoteca.web.negocio.exception.DuplicateException;
import com.finProyecto.fimoteca.web.negocio.exception.FileException;
import com.finProyecto.fimoteca.web.negocio.exception.NotFoundException;
import com.finProyecto.fimoteca.web.vo.dto.FilmDTO;

public interface IFilmService {

	void registrarPelicula(FilmDTO filmDTO) throws DuplicateException;

	void subirDocumento(MultipartFile multipartFile) throws FileException;

	Set<FilmDTO> busquedaPeliculasPorNombre(String title);

	Set<FilmDTO> buscarPeliculaPorTituloInicial(String inicialTitulo);
	
	FilmDTO buscarPeliculaPorId(long id) throws NotFoundException;
}
