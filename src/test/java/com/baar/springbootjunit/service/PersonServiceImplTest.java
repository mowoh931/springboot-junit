package com.baar.springbootjunit.service;


import static org.junit.Assert.assertNotNull;

import com.baar.springbootjunit.dto.PersonDto;
import com.baar.springbootjunit.exception.PersonExistsException;
import com.baar.springbootjunit.model.Person;
import com.baar.springbootjunit.repository.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class PersonServiceImplTest {

  @Mock private PersonRepository repository;

  @InjectMocks private PersonServiceImpl service;

  ModelMapper modelMapper = new ModelMapper();

  @Before
  public void setUp() throws Exception {
    Person person = new Person(1, "John", "John's City");
  }

  @Test
  public void createPerson() throws PersonExistsException {
    Person person = new Person(1, "John", "John's City");
    Mockito.when(repository.save(person)).thenReturn(person);
    assertNotNull(service.createPerson(modelMapper.map(person, PersonDto.class)));

  }

  @Test
  public void getPersons() {

  }

  @Test
  public void getPerson() {}

  @Test
  public void updatePerson() {}

  @Test
  public void deletePerson() {}
}
