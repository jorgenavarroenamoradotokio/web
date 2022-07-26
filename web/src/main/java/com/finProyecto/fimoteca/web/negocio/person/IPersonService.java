package com.finProyecto.fimoteca.web.negocio.person;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.finProyecto.fimoteca.web.negocio.exception.DuplicateException;
import com.finProyecto.fimoteca.web.vo.dto.PersonDTO;

public interface IPersonService {

	public Set<PersonDTO> buscarPersonMasivoPorId(Collection<Long> id);

	public PersonDTO buscarPersonPorId(long id);

	public Map<Long, Collection<PersonDTO>> obtenerTodasPersonasAgrupadasPorTipoPersona();

	public boolean registrarPersona(PersonDTO personDTO) throws DuplicateException;

}
