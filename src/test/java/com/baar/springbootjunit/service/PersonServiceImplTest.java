package com.baar.springbootjunit.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.baar.springbootjunit.dto.PersonDto;
import com.baar.springbootjunit.exception.PersonExistsException;
import com.baar.springbootjunit.exception.PersonNotFoundException;
import com.baar.springbootjunit.model.Person;
import com.baar.springbootjunit.repository.PersonRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

/*
 * @RunWith(MockitoJUnitRunner.Silent.class)
 * Used when testing the Junit version <4
 */
@ExtendWith(MockitoExtension.class)
public class PersonServiceImplTest {
  public PersonServiceImplTest() {}

  @Mock private PersonRepository repository;

  @InjectMocks private PersonServiceImpl service;

  ModelMapper modelMapper = new ModelMapper();

  @BeforeEach
  public void setUp() throws Exception {

    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void createPerson() throws PersonNotFoundException, PersonExistsException {
    Person person = new Person(1, "John", "John's City");
    when(repository.save(person)).thenReturn(person);

    Person actual = service.createPerson(modelMapper.map(person, PersonDto.class));

    Assertions.assertNotNull(actual, "Expected does not equals actual");
    Assertions.assertEquals("John", actual.getName(), "Expected equals actual");
    Mockito.verify(repository, Mockito.times(1)).save(person);
  }

  @Test
  public void getPersons() {
    Person john = new Person(1, "John", "John's City");
    Person doe = new Person(2, "Doe", "Doe's City");
    List<PersonDto> expected =
        List.of(john, doe).stream()
            .map((element) -> modelMapper.map(element, PersonDto.class))
            .collect(Collectors.toList());

    when(repository.findAll()).thenReturn(List.of(john, doe));

    List<PersonDto> actual = service.getPersons();
    assertEquals(expected, service.getPersons(), "Expected equals actual");
    assertEquals(expected.size(), actual.size(), "Expected equals actual");
    Mockito.verify(repository, Mockito.atLeastOnce()).findAll();
  }

  @Test
  public void getPerson() throws PersonNotFoundException {
    Person john = new Person(1, "John", "John's City");

    when(repository.findById(1)).thenReturn(Optional.of(john));

    PersonDto actual = service.getPerson(1);
    Assertions.assertEquals(
        modelMapper.map(john, PersonDto.class), actual, "Expected equals actual");
    Assertions.assertNotNull(actual, "should not be null");
  }

  @Test
  public void findByNameAndCity() throws PersonNotFoundException {
    Person john = new Person(1, "John", "John's City");
    PersonDto johnDto = modelMapper.map(john, PersonDto.class);

    when(repository.findByNameIgnoreCaseAndCityIgnoreCase("John", "John's City"))
        .thenReturn(Optional.ofNullable(john));

    PersonDto actual = service.findByNameAndCity("John", "John's City");
    Assertions.assertNotNull(actual, "should not be null");
    Assertions.assertEquals(johnDto, actual, "Expected equals actual");
  }

  @Test
  public void updatePerson() throws PersonNotFoundException {
    Person john = new Person(1, "John", "John's City");
    Person john_ = new Person(1, "John", "John's New City");
    PersonDto john_Dto = modelMapper.map(john_, PersonDto.class);

    when(repository.findById(1)).thenReturn(Optional.of(john));
    when(repository.save(john_)).thenReturn(john_);

    service.updatePerson(1, john_Dto);
    Mockito.verify(repository, Mockito.atLeastOnce()).save(john_);
  }

  @Test
  public void deletePerson() throws PersonNotFoundException {
    Person john = new Person(1, "John", "John's City");

    repository.save(john);
    repository.deleteById(1);
    Optional<Person> c = repository.findById(1);
    assertEquals(Optional.empty(), c);

    when(repository.findById(1)).thenReturn(Optional.of(john));
    assertAll(() -> service.deletePerson(1));

    Mockito.verify(repository, Mockito.times(1)).delete(john);
  }
}
