package com.finProyecto.fimoteca.web.persistencia.person;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finProyecto.fimoteca.web.vo.TypePersonEnum;

@Repository
public interface IPersonRepository extends CrudRepository<Person, Long> {

	@Query("select p from Person p where p.id in :list")
	Optional<Set<Person>> buscarPersonaPorIdMasivo(@Param("list") Collection<Long> list);

	@Query(value = "select p from Person p WHERE p.name = :name and p.surname = :surname and p.typePerson = :tipo ")
	Optional<Person> finByNameAndSurnameAndTypePerson(@Param("name") String nombre, @Param("surname") String apellido, @Param("tipo") TypePersonEnum typePersona);

}
