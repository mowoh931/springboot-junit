package com.baar.springbootjunit.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.baar.springbootjunit.dto.PersonDto;
import com.baar.springbootjunit.exception.PersonNotFoundException;
import com.baar.springbootjunit.model.Person;
import com.baar.springbootjunit.service.PersonServiceImpl;
import com.baar.springbootjunit.util.ExceptionMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(PersonApi.class)
class PersonApiTest {
  private ModelMapper modelMapper = new ModelMapper();
  @Autowired private MockMvc mockMvc;
  @MockBean
  private PersonServiceImpl service;
  @Autowired private ObjectMapper objectMapper;
  @InjectMocks
  private PersonApi personApi;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createPerson() throws Exception {
    Person person = new Person(1, "John", "John's City");
    PersonDto personDto = new ModelMapper().map(person, PersonDto.class);
    lenient().when(service.createPerson(any(PersonDto.class))).thenReturn(person);

    ResponseEntity<Person> responseEntity = personApi.createPerson(personDto);
    Person responseBody = responseEntity.getBody();
    int responseCode = responseEntity.getStatusCode().value();

    assertNotNull(responseBody);
    assertEquals(HttpStatus.CREATED.value(), responseCode);

    String url = "/api/persons/save";
    ResultActions resultActions =
        mockMvc.perform(
            post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(personDto)));

    resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
    MvcResult mvcResult = resultActions.andReturn();
    responseCode = mvcResult.getResponse().getStatus();
    assertEquals(HttpStatus.CREATED.value(), responseCode);
  }

  @Test
  void getPerson() throws Exception {
    Person person = new Person(1, "John", "John's City");
    PersonDto personDto = new ModelMapper().map(person, PersonDto.class);

    when(service.getPerson(1)).thenReturn(personDto);

    ResponseEntity<PersonDto> responseEntity = personApi.getPerson(1);
    assertEquals(personDto, responseEntity.getBody());
    assertNotEquals(personApi.getPerson(2), responseEntity);

    String url = "/api/persons/get/one/person/id/";

    int id = 1;
    ResultActions resultActions =
        mockMvc.perform(
            get(url + id)
                .content(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(personDto)));

    MvcResult mvcResult = resultActions.andReturn();

    int responseCode = mvcResult.getResponse().getStatus();

    assertEquals(HttpStatus.OK.value(), responseCode);
    assertNotEquals(HttpStatus.CREATED.value(), responseCode);
  }

  @Test
  void findByNameAndCity() throws Exception {
    Person person = new Person(1, "John", "John's City");
    PersonDto personDto = modelMapper.map(person, PersonDto.class);
    when(service.findByNameAndCity("John", "John's City")).thenReturn(personDto);

    ResponseEntity<PersonDto> responseEntity = personApi.findByNameAndCity("John", "John's City");
    PersonDto responseBody = responseEntity.getBody();
    assertEquals(personDto, responseBody, "Asserts that response equals expected.");

    // TODO: 12/16/2023
    String url = "/api/persons/get/one/person/name/city/{name}/{city}?name=John&city=John's City";
    ResultActions resultActions =
        mockMvc.perform(
            get(url, "name", "city")
                .content(objectMapper.writeValueAsString(personDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    MvcResult mvcResult = resultActions.andReturn();
    int responseCode = mvcResult.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), responseCode);

    String content = mvcResult.getResponse().getContentAsString();
    assertEquals(content, objectMapper.writeValueAsString(personDto));
  }

  @Test
  void test_invalid_name_and_city() throws Exception {
    when(service.findByNameAndCity("John", "John's City"))
        .thenThrow(new PersonNotFoundException(ExceptionMessage.PERSON_NOT_FOUND));

    assertThrows(
        PersonNotFoundException.class,
        () -> {
          personApi.findByNameAndCity("John", "John's City");
        },
        "Asserts that PersonNotFoundException is thrown.");
  }

  @Test
  void updatePerson() throws Exception {

    Person person = new Person(1, "John", "John's City");
    PersonDto personDto = new PersonDto(1, "John", "John's New City");
    when(service.getPerson(1)).thenReturn(personDto);
    service.updatePerson(1, personDto);
    verify(service, atLeastOnce()).updatePerson(1, personDto);

    String url = "/api/persons/update/person/id/1";
    ResultActions resultActions =
        mockMvc.perform(
            put(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(personDto)));
    MvcResult mvcResult = resultActions.andReturn();
    int responseCode = mvcResult.getResponse().getStatus();
    assertEquals(HttpStatus.CREATED.value(), responseCode);
  }

  @Test
  void deletePerson() throws Exception {
    Person person = new Person(1, "John", "John's City");
    PersonDto personDto = new ModelMapper().map(person, PersonDto.class);
    when(service.getPerson(1)).thenReturn(personDto);

    service.deletePerson(1);
    verify(service, atLeastOnce()).deletePerson(1);
    assertAll(() -> personApi.deletePerson(1));

    String url = "/api/persons/delete/person/id/1";
    ResultActions resultActions = mockMvc.perform(delete(url).contentType("application/json"));

    MvcResult mvcResult = resultActions.andReturn();
    int statusCode = mvcResult.getResponse().getStatus();
    assertEquals(HttpStatus.OK.value(), statusCode);
  }

  @Test
  void getPersons() throws Exception {

    Person person = new Person(1, "John", "John's City");
    PersonDto personDto = new PersonDto(1, "John", "John's City");
    List<PersonDto> persons = List.of(personDto);
    when(service.getPersons()).thenReturn(persons);

    ResultActions resultActions =
        mockMvc.perform(
            get("/api/persons/get/all")
                .content(objectMapper.writeValueAsString(persons))
                .contentType(MediaType.APPLICATION_JSON));

    MockHttpServletResponse response = resultActions.andReturn().getResponse();
    int statusCode = response.getStatus();
    assertEquals(HttpStatus.OK.value(), statusCode);
    assertEquals(
        objectMapper.writeValueAsString(persons), response.getContentAsString());
  }
}
