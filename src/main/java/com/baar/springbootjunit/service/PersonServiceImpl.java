package com.baar.springbootjunit.service;

import com.baar.springbootjunit.dto.PersonDto;
import com.baar.springbootjunit.exception.PersonExistsException;
import com.baar.springbootjunit.exception.PersonNotFoundException;
import com.baar.springbootjunit.model.Person;
import com.baar.springbootjunit.repository.PersonRepository;
import com.baar.springbootjunit.util.ExceptionMessage;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service(value = "service")
// @Transactional
public class PersonServiceImpl implements PersonService {

 private PersonRepository repository;

  private  ModelMapper mapper = new ModelMapper();

  private final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

  public PersonServiceImpl(PersonRepository repository) {
    this.repository = repository;
  }

  @Override
  public Person createPerson(PersonDto personDto) throws PersonExistsException {

    Optional<Person> optional = repository.findById(personDto.getId());
    if (optional.isPresent()) {
      throw new PersonExistsException(ExceptionMessage.PERSON_EXISTS);
    }
    Person person = new Person(personDto.getId(), personDto.getName(), personDto.getCity());
    repository.save(person);
    return person;
  }

  @Override
  public List<PersonDto> getPersons() {
    return repository.findAll().stream()
        .map(element -> mapper.map(element, PersonDto.class))
        .toList();
  }

  @Override
  public PersonDto getPerson(Integer id) throws PersonNotFoundException {
    return repository
        .findById(id)
        .map(person -> mapper.map(person, PersonDto.class))
        .orElseThrow(() -> new PersonNotFoundException(ExceptionMessage.PERSON_NOT_FOUND));
  }

  @Override
  public void updatePerson(Integer id, PersonDto personDto) throws PersonNotFoundException {
    repository.save(
            repository
            .findById(id)
            .map(person -> mapper.map(personDto, Person.class))
            .orElseThrow(() -> new PersonNotFoundException(ExceptionMessage.PERSON_NOT_FOUND)));
    LOGGER.info("Updated person successfully {}", id);
  }

  @Override
  public void deletePerson(Integer id) throws PersonNotFoundException {
    repository.delete(
            repository
            .findById(id)
            .orElseThrow(() -> new PersonNotFoundException(ExceptionMessage.PERSON_NOT_FOUND)));
    LOGGER.info("Deleted person successfully {}", id);
  }
}
