package com.baar.springbootjunit.api;

import com.baar.springbootjunit.dto.PersonDto;
import com.baar.springbootjunit.exception.PersonExistsException;
import com.baar.springbootjunit.exception.PersonNotFoundException;
import com.baar.springbootjunit.model.Person;
import com.baar.springbootjunit.service.PersonServiceImpl;
import jakarta.websocket.server.PathParam;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/persons")
@CrossOrigin(value = "*")
public class PersonApi {

  private PersonServiceImpl service;

  public PersonApi(PersonServiceImpl service) {
    this.service = service;
  }

  @PostMapping(
      value = "/save",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Person> createPerson(@RequestBody PersonDto personDto)
      throws PersonExistsException {
    return new ResponseEntity<>(service.createPerson(personDto), HttpStatus.CREATED);
  }

  @GetMapping(value = "/get/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<PersonDto>> getPersons() {
    return new ResponseEntity<>(service.getPersons(), HttpStatus.OK);
  }

  @GetMapping(value = "/get/one/person/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PersonDto> getPerson(@PathVariable Integer id)
      throws PersonNotFoundException {
    return new ResponseEntity<>(service.getPerson(id), HttpStatus.OK);
  }

  @GetMapping(
      value = "/get/one/person/name/city/{name}/{city}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PersonDto> findByNameAndCity(
      @PathParam(value = "name") String name, @PathParam(value = "city") String city)
      throws PersonNotFoundException {
    return new ResponseEntity<>(service.findByNameAndCity(name, city), HttpStatus.OK);
  }

  @PutMapping(
      value = "/update/person/id/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> updatePerson(
      @PathVariable Integer id, @RequestBody PersonDto personDto) throws PersonNotFoundException {
    service.updatePerson(id, personDto);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/delete/person/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> deletePerson(@PathVariable Integer id)
      throws PersonNotFoundException {
    service.deletePerson(id);
    return new ResponseEntity<>("Deleted person with id: " + id, HttpStatus.OK);
  }
}
