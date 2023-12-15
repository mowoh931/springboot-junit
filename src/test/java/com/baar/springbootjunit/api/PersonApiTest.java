package com.baar.springbootjunit.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.baar.springbootjunit.dto.PersonDto;
import com.baar.springbootjunit.exception.PersonNotFoundException;
import com.baar.springbootjunit.model.Person;
import com.baar.springbootjunit.service.PersonServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(PersonApi.class)
class PersonApiTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private PersonServiceImpl service;

  @Autowired private ObjectMapper objectMapper;

  @InjectMocks private PersonApi personApi;

  ModelMapper modelMapper = new ModelMapper();

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void createPerson() throws Exception {
    Person person = new Person(1, "John", "John's City");
    PersonDto personDto = new ModelMapper().map(person, PersonDto.class);
    Mockito.lenient().when(service.createPerson(any(PersonDto.class))).thenReturn(person);

    ResponseEntity<Person> responseEntity = personApi.createPerson(personDto);
    Person responseBody = responseEntity.getBody();
    int responseCode = responseEntity.getStatusCode().value();

    Assertions.assertNotNull(responseBody);
    assertEquals(HttpStatus.CREATED.value(), responseCode);

    String url = "http://localhost:8081/api/persons/save";
    ResultActions resultActions =
        mockMvc.perform(
            post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(personDto)));

    resultActions.andExpect(MockMvcResultMatchers.status().isCreated());

    responseCode = resultActions.andReturn().getResponse().getStatus();
    assertEquals(HttpStatus.CREATED.value(), responseCode);
  }

  @Test
  void getPerson() throws Exception {
    Person person = new Person(1, "John", "John's City");
    PersonDto personDto = new ModelMapper().map(person, PersonDto.class);
    Mockito.when(service.getPerson(1)).thenReturn(personDto);

    ResponseEntity<PersonDto> responseEntity = personApi.getPerson(1);
    assertEquals(personDto, responseEntity.getBody());
    Assertions.assertNotEquals(personApi.getPerson(2), responseEntity);

    String url = "http://localhost:8081/api/persons/get/one/person/id/";

    int id = 1;
    ResultActions resultActions =
        mockMvc.perform(
            get(url + id)
                .content(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(personDto)));

    int status = resultActions.andReturn().getResponse().getStatus();

    assertEquals(HttpStatus.OK.value(), status);
    Assertions.assertNotEquals(HttpStatus.CREATED.value(), status);

    String c1 = (resultActions.andReturn().getResponse().getContentAsString());
    String c2 = objectMapper.writeValueAsString(personDto);
  }

  @Test
  public void findByNameAndCity() {}

  @Test
  void updatePerson() throws Exception {

    Person person = new Person(1, "John", "John's City");
    PersonDto personDto = new PersonDto(1, "John", "John's New City");
    //    when(service.getPerson(1)).thenReturn(personDto);
    //    service.updatePerson(1, personDto);
    //    verify(service, atLeastOnce()).updatePerson(1, personDto);
    String url = "/api/persons/update/person/id/";
    System.out.println(
        mockMvc
            .perform(
                put(url + 1)
                    .content(objectMapper.writeValueAsString(personDto))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn()
            .getModelAndView());
  }

  @Test
  void deletePerson() throws Exception {
    Person person = new Person(1, "John", "John's City");
    PersonDto personDto = new ModelMapper().map(person, PersonDto.class);
    Mockito.when(service.getPerson(1)).thenReturn(personDto);

    // TODO: 12/14/2023
    service.deletePerson(1);
    Mockito.verify(service, atLeastOnce()).deletePerson(1);
    Assertions.assertAll(() -> personApi.deletePerson(1));

    String url = "/api/persons/delete/person/id/1";
    MvcResult c =
        mockMvc
            .perform(
                delete(url)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(person)))
            .andReturn();
  }
}
