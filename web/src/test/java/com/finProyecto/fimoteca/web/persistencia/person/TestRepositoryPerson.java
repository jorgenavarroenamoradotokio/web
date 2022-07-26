package com.finProyecto.fimoteca.web.persistencia.person;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.finProyecto.fimoteca.web.vo.TypePersonEnum;

@SpringBootTest
@RunWith(JUnit4.class)
class TestRepositoryPerson {

	@Autowired
	IPersonRepository repository;
	
	@Test
	public void personFindMasiveIdEmpty() {
		Optional<Set<Person>> opt = repository.buscarPersonaPorIdMasivo(new ArrayList<>());
		assertTrue(opt.get().isEmpty());
	}
	
	@Test
	public void personFindMasiveIdEmptyResult() {
		Collection<Long> list = new ArrayList<>();
		list.add(13L);
		list.add(144L);
		Optional<Set<Person>> opt = repository.buscarPersonaPorIdMasivo(list);
		assertTrue(opt.get().isEmpty());
	}
	
	@Test
	public void personFindMasiveId() {
		Person person = repository.save(Person.builder().name("test").surname("test").typePerson(TypePersonEnum.ACTOR).build());
		Collection<Long> list = new ArrayList<>();
		list.add(person.getId());
		list.add(144L);
		list.add(2211L);
		Optional<Set<Person>> opt = repository.buscarPersonaPorIdMasivo(list);
		assertTrue(opt.isPresent());
		repository.delete(person);
	}
	
	@Test
	public void personFindNameSurnameType() {
		Person person = repository.save(Person.builder().name("test").surname("test").typePerson(TypePersonEnum.ACTOR).build());
		Optional<Person> opt = repository.finByNameAndSurnameAndTypePerson(person.getName(), person.getSurname(), person.getTypePerson());
		assertTrue(opt.isPresent());
		repository.delete(person);
	}
	
	@Test
	public void personNotFindNameSurnameType() {
		Person person = Person.builder().name("test").surname("test").typePerson(TypePersonEnum.ACTOR).build();
		Optional<Person> opt = repository.finByNameAndSurnameAndTypePerson(person.getName(), person.getSurname(), person.getTypePerson());
		assertTrue(opt.isEmpty());
	}
}
