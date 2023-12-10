package com.baar.springbootjunit.service;

import com.baar.springbootjunit.dto.PersonDto;
import com.baar.springbootjunit.exception.PersonExistsException;
import com.baar.springbootjunit.exception.PersonNotFoundException;
import com.baar.springbootjunit.model.Person;
import com.baar.springbootjunit.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

  @Autowired PersonRepository repository;

  @Override
  public Person createPerson(PersonDto personDto) throws PersonExistsException {

    Optional<Person> optional = repository.findById(personDto.getId());
    if (optional.isPresent()) {
      throw new PersonExistsException("Person already exists");
    }
    Person person = new Person(personDto.getId(), personDto.getName(), personDto.getCity());
    repository.save(person);
    return person;
  }
}
