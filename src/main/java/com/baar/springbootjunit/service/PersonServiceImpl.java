package com.baar.springbootjunit.service;

import com.baar.springbootjunit.dto.PersonDto;
import com.baar.springbootjunit.exception.PersonExistsException;
import com.baar.springbootjunit.exception.PersonNotFoundException;
import com.baar.springbootjunit.model.Person;
import com.baar.springbootjunit.repository.PersonRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "personService")
// @Transactional
public class PersonServiceImpl implements PersonService {

  @Autowired private PersonRepository personRepository;

  private  ModelMapper mapper = new ModelMapper();

  private final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

  public PersonServiceImpl(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  @Override
  public Person createPerson(PersonDto personDto) throws PersonExistsException {

    Optional<Person> optional = personRepository.findById(personDto.getId());
    if (optional.isPresent()) {
      throw new PersonExistsException("Person already exists");
    }
    Person person = new Person(personDto.getName(), personDto.getCity());
    personRepository.save(person);
    return person;
  }

  @Override
  public List<PersonDto> getPersons() {
    return personRepository.findAll().stream()
        .map((element) -> mapper.map(element, PersonDto.class))
        .collect(Collectors.toList());
  }

  @Override
  public PersonDto getPerson(Integer id) throws PersonNotFoundException {
    return personRepository
        .findById(id)
        .map(person -> mapper.map(person, PersonDto.class))
        .orElseThrow(() -> new PersonNotFoundException("person not found"));
  }

  @Override
  public void updatePerson(Integer id, PersonDto personDto) throws PersonNotFoundException {
    personRepository.save(
        personRepository
            .findById(id)
            .map(person -> mapper.map(personDto, Person.class))
            .orElseThrow(() -> new PersonNotFoundException("person not found")));
    LOGGER.info("Updated person successfully {}", id);
  }

  @Override
  public void deletePerson(Integer id) throws PersonNotFoundException {
    personRepository.delete(
        personRepository
            .findById(id)
            .orElseThrow(() -> new PersonNotFoundException("person not found")));
    LOGGER.info("Deleted person successfully {}", id);
  }
}
