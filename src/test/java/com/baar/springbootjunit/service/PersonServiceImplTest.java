package com.baar.springbootjunit.service;

import static org.junit.Assert.*;

import com.baar.springbootjunit.dto.PersonDto;
import com.baar.springbootjunit.exception.PersonExistsException;
import com.baar.springbootjunit.exception.PersonNotFoundException;
import com.baar.springbootjunit.model.Person;
import com.baar.springbootjunit.repository.PersonRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

// @ExtendWith(MockitoExtension.class)

@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class PersonServiceImplTest {
  public PersonServiceImplTest() {
  }

  @Mock private PersonRepository repository;

  @InjectMocks private PersonServiceImpl service;

  ModelMapper modelMapper = new ModelMapper();

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void createPerson() throws PersonExistsException {
    Person person = new Person(1, "John", "John's City");
    Mockito.when(repository.save(person)).thenReturn(person);

    Person actual = service.createPerson(modelMapper.map(person, PersonDto.class));
    assertNotNull("Expected does not equals actual",actual);
    Assert.assertEquals("Expected equals actual",person, actual);

  }

  @Test
  public void getPersons() {
    Person john = new Person(1, "John", "John's City");
    Person doe = new Person(2, "Doe", "Doe's City");
    List<PersonDto> expected =
        List.of(john, doe).stream()
            .map((element) -> modelMapper.map(element, PersonDto.class))
            .collect(Collectors.toList());

    Mockito.when(repository.findAll()).thenReturn(List.of(john, doe));

    List<PersonDto> actual = service.getPersons();

    Assert.assertEquals("Expected equals actual",expected, service.getPersons());
    Assert.assertEquals("Expected equals actual",expected.size(), actual.size());
    Assert.assertEquals("Expected equals actual",expected.get(0), actual.get(0));
  }

  @Test
  public void getPerson() throws PersonNotFoundException {
    Person john = new Person(1, "John", "John's City");
    Mockito.when(repository.findById(1)).thenReturn(Optional.of(john));

    PersonDto actual = service.getPerson(1);
    Assert.assertEquals("Expected equals actual",modelMapper.map(john, PersonDto.class), actual);
  }

  @Test
  public void updatePerson() throws PersonNotFoundException {

    Person john = new Person(1, "John", "John's City");
    Person john_ = new Person(1, "John", "John's New City");
    Mockito.when(repository.save(john)).thenReturn(john);

    Mockito.when(repository.findById(1)).thenReturn(Optional.of(john));
    Mockito.when(repository.save(john_)).thenReturn(john_);

    service.updatePerson(1, modelMapper.map(john_, PersonDto.class));
    Mockito.verify(repository, Mockito.times(1)).save(john_);
  }

  @Test
  public void deletePerson() throws PersonNotFoundException {
    Person john = new Person(1, "John", "John's City");

    Mockito.when(repository.findById(1)).thenReturn(Optional.of(john));
    service.deletePerson(john.getId());

    Mockito.verify(repository, Mockito.times(1)).delete(john);
  }
}
