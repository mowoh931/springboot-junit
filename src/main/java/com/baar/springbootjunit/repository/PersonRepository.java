package com.baar.springbootjunit.repository;

import com.baar.springbootjunit.model.Person;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository(value = "personRepository")
public interface PersonRepository extends JpaRepository<Person, Integer> {
  @Query("select p from Person p where upper(p.name) = upper(?1) and upper(p.city) = upper(?2)")
  Optional<Person> findByNameIgnoreCaseAndCityIgnoreCase(String name, String city);
}
