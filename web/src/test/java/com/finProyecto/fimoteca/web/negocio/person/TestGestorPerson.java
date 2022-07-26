package com.finProyecto.fimoteca.web.negocio.person;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.finProyecto.fimoteca.web.negocio.exception.DuplicateException;
import com.finProyecto.fimoteca.web.persistencia.person.IPersonRepository;
import com.finProyecto.fimoteca.web.persistencia.person.Person;
import com.finProyecto.fimoteca.web.utils.Utilidades;
import com.finProyecto.fimoteca.web.vo.TypePersonEnum;
import com.finProyecto.fimoteca.web.vo.dto.PersonDTO;

@SpringBootTest
@RunWith(JUnit4.class)
class TestGestorPerson {

	@Autowired
	Utilidades utils;

	@Mock
	IPersonRepository repositoryMock;

	@Test
	public void personFindMasiveIdNull() {
		GestorPerson gestorPerson = new GestorPerson();
		gestorPerson.utils = utils;
		Set<PersonDTO> result = gestorPerson.buscarPersonMasivoPorId(null);
		assertTrue(result.isEmpty());
	}

	@Test
	public void personFindMasiveIdEmpty() {
		GestorPerson gestorPerson = new GestorPerson();
		gestorPerson.utils = utils;
		Set<PersonDTO> result = gestorPerson.buscarPersonMasivoPorId(new HashSet<>());
		assertTrue(result.isEmpty());
	}

	@Test
	public void personFindMasiveIdNotFound() {
		when(repositoryMock.buscarPersonaPorIdMasivo(Mockito.anyCollection())).thenReturn(Optional.empty());

		GestorPerson gestorPerson = new GestorPerson();
		gestorPerson.utils = utils;
		gestorPerson.repositorio = repositoryMock;
		Set<PersonDTO> result = gestorPerson.buscarPersonMasivoPorId(cargaIds(1L, 2L, 3L, 4L));
		assertTrue(result.isEmpty());
	}

	@Test
	public void personFindMasiveIdFound() {
		Optional<Set<Person>> opt = Optional.of(cargaListaPerson(person("asd", "asd", 1)));
		when(repositoryMock.buscarPersonaPorIdMasivo(Mockito.anyCollection())).thenReturn(opt);
		GestorPerson gestorPerson = new GestorPerson();
		gestorPerson.utils = utils;
		gestorPerson.repositorio = repositoryMock;
		Set<PersonDTO> result = gestorPerson.buscarPersonMasivoPorId(cargaIds(1L, 2L, 3L, 4L));
		assertTrue(!result.isEmpty());
		assertTrue(result.size() == 1);
	}

	@Test
	public void personFindByIdFound() {
		Optional<Set<Person>> opt = Optional.of(cargaListaPerson(person("asd", "asd", 1)));
		when(repositoryMock.buscarPersonaPorIdMasivo(Mockito.anyCollection())).thenReturn(opt);
		GestorPerson gestorPerson = new GestorPerson();
		gestorPerson.utils = utils;
		gestorPerson.repositorio = repositoryMock;
		Set<PersonDTO> result = gestorPerson.buscarPersonMasivoPorId(cargaIds(1L, 2L, 3L, 4L));
		assertTrue(!result.isEmpty());
		assertTrue(result.size() == 1);
	}

	@Test
	public void personFindByIdNotFound() {
		when(repositoryMock.buscarPersonaPorIdMasivo(Mockito.anyCollection())).thenReturn(Optional.ofNullable(null));
		GestorPerson gestorPerson = new GestorPerson();
		gestorPerson.utils = utils;
		gestorPerson.repositorio = repositoryMock;
		Set<PersonDTO> result = gestorPerson.buscarPersonMasivoPorId(cargaIds(1L, 2L, 3L, 4L));
		assertTrue(result.isEmpty());
	}

	@Test
	public void personNotMapTypePerson() {
		when(repositoryMock.findAll()).thenReturn(null);
		GestorPerson gestorPerson = new GestorPerson();
		gestorPerson.utils = utils;
		gestorPerson.repositorio = repositoryMock;
		Map<Long, Collection<PersonDTO>> result = gestorPerson.obtenerTodasPersonasAgrupadasPorTipoPersona();
		assertTrue(result.isEmpty());
	}

	@Test
	public void personMapTypePerson() {
		when(repositoryMock.findAll()).thenReturn(cargaListaPerson(person("asd", "asd", 1)));
		GestorPerson gestorPerson = new GestorPerson();
		gestorPerson.utils = utils;
		gestorPerson.repositorio = repositoryMock;
		Map<Long, Collection<PersonDTO>> result = gestorPerson.obtenerTodasPersonasAgrupadasPorTipoPersona();
		assertTrue(!result.isEmpty());
		assertTrue(result.get(TypePersonEnum.getInstance(1).getId()).size() == 1);
	}

	@Test()
	public void personDuplicateInsert() {
		Person p = person("asd", "asd", 1);
		Optional<Person> opt = Optional.of(p);

		when(repositoryMock.finByNameAndSurnameAndTypePerson(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(opt);

		GestorPerson gestorPerson = new GestorPerson();
		gestorPerson.utils = utils;
		gestorPerson.repositorio = repositoryMock;

		DuplicateException thrown = assertThrows(DuplicateException.class, () -> {
			gestorPerson.registrarPersona(PersonDTO.builder().name("").surname("").typePerson(null).build());
		}, "Error");

		assertNotNull(thrown);
	}

	private List<Long> cargaIds(Long... ids) {
		List<Long> result = new ArrayList<>();
		for (Long id : ids) {
			result.add(id);
		}
		return result;
	}

	private Set<Person> cargaListaPerson(Person... persons) {
		Set<Person> result = new HashSet<>();
		for (Person person : persons) {
			result.add(person);
		}
		return result;
	}

	private Person person(String name, String surname, long idTypePerson) {
		return Person.builder().name(name).surname(surname).typePerson(TypePersonEnum.getInstance(idTypePerson)).build();
	}
}