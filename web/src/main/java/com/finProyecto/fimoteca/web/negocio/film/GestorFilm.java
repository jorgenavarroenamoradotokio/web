package com.finProyecto.fimoteca.web.negocio.film;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.finProyecto.fimoteca.web.negocio.exception.DuplicateException;
import com.finProyecto.fimoteca.web.negocio.exception.FileException;
import com.finProyecto.fimoteca.web.negocio.exception.NotFoundException;
import com.finProyecto.fimoteca.web.negocio.person.GestorPerson;
import com.finProyecto.fimoteca.web.persistencia.film.Film;
import com.finProyecto.fimoteca.web.persistencia.film.IFilmRepository;
import com.finProyecto.fimoteca.web.utils.Constantes;
import com.finProyecto.fimoteca.web.utils.LectorPropiedades;
import com.finProyecto.fimoteca.web.utils.Utilidades;
import com.finProyecto.fimoteca.web.vo.dto.FilmDTO;

@Service
public class GestorFilm implements IFilmService {

	private final static Logger logger = LoggerFactory.getLogger(GestorPerson.class);

	@Autowired
	Utilidades utils;
	
	@Autowired
	LectorPropiedades lp;
	
	@Autowired
	IFilmRepository repository;
	
	
	/**
	 * 
	 * Iniciamos el proceso de registro de una pelicula.
	 * Se busca en los sistemas si ya existe una pelicula con dicho nombre
	 * Si existe se lanza una excepcion, si no existe se registra
	 * 
	 */
	
	@Override
	public void registrarPelicula(FilmDTO filmDTO) throws DuplicateException {
		logger.info("Iniciamos el proceso de registro de informacion");
		if (repository.findByTitle(filmDTO.getTitle()).isPresent()) {
			throw new DuplicateException();
		}
		repository.save(utils.mapperFilm(filmDTO));
	}

	/**
	 * 
	 * Iniciamos el proceso de subida del poster promocional de la pelicula al servidor
	 * 
	 */

	@Override
	public void subirDocumento(MultipartFile multiparFile) throws FileException {
		logger.info("Iniciamos el proceso de subida de documento");
		
		if(multiparFile != null) {
			// Obtenemos la ubicacion donde esta la app ejecutandose
			String directorioEjecucion = System.getProperty("user.dir");
			
			String directorioFilesCompleto = directorioEjecucion
					+ lp.getValorPropiedad(Constantes.PROPIEDAD_DIRECTORIO_CONFIGURACION_DOCUMENTOS) + File.separator
					+ lp.getValorPropiedad(Constantes.PROPIEDAD_DIRECTORIO_SUBIDA_DOCUMENTOS);
			
			// Creamos la ruta
			File directorio = new File(directorioFilesCompleto);
			if (!directorio.exists()) {
				if (directorio.mkdirs()) {
					logger.info("Directorio " + directorio.getAbsolutePath() + ", creado con exito");
				} else {
					logger.error("El directorio " + directorio.getAbsolutePath() + " no se pudo crear");
				}
			}
			
			// Copiamos y renombramos el documento
			Path copyLocation = Paths.get(directorioFilesCompleto + File.separator + multiparFile.getOriginalFilename());
			try {
				Files.copy(multiparFile.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new FileException();
			}			
		}
	}

	/**
	 * 
	 * Obtenemos un listado de todas las peliculas cuyo nombre contenga la palabra
	 * insertada
	 * 
	 */
	
	@Override
	public Set<FilmDTO> busquedaPeliculasPorNombre(String title) {
		logger.info("Buscamos todas las peliculas cuyo nombre contenga una palabra en su nombre");
		Set<Film> film = repository.findByTitleStartsWith(title);
		Set<FilmDTO> resultado = new HashSet<>();
		film.forEach(element -> resultado.add(utils.mapperFilm(element)));
		return resultado;
	}

	/**
	 * 
	 * Obtenemos un listado de peliculas cuyo nombre empiece por el texto que se ha
	 * insertado
	 * 
	 */
	
	@Override
	public Set<FilmDTO> buscarPeliculaPorTituloInicial(String inicialTitulo) {
		logger.info("Buscamos todas las peliculas cuyo nombre empiece por la letra insertada");
		Set<Film> film = repository.findByTitleStartsWith(inicialTitulo);
		Set<FilmDTO> resultado = new HashSet<>();
		film.forEach(element -> resultado.add(utils.mapperFilm(element)));
		return resultado;
	}

	/**
	 * 
	 * Buscamos toda la informacion asociada a una pelicula por ID
	 * 
	 */
	
	@Override
	public FilmDTO buscarPeliculaPorId(long id) throws NotFoundException {
		logger.info("Obtenemos toda la informacion de la una pelicula por ID");
		Film film =repository.findById(id).orElseThrow(()-> new NotFoundException());
		return utils.mapperFilm(film);
	}
}
