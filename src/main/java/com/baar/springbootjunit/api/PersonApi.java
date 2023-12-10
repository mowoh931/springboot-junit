package com.baar.springbootjunit.api;

import com.baar.springbootjunit.dto.PersonDto;
import com.baar.springbootjunit.exception.PersonExistsException;
import com.baar.springbootjunit.exception.PersonNotFoundException;
import com.baar.springbootjunit.model.Person;
import com.baar.springbootjunit.service.PersonServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/persons")
public class PersonApi {

  private   PersonServiceImpl personService;

  public PersonApi(PersonServiceImpl personService) {
    this.personService = personService;
  }

  @PostMapping("/save")
  public Person createPerson(@RequestBody PersonDto personDto) throws PersonExistsException {
    return personService.createPerson(personDto);
  }

  @GetMapping("/get/all")
  public List<PersonDto> getPersons() {
    return personService.getPersons();
  }

  @GetMapping("/get/one/person/id/{id}")
  public PersonDto getPerson(@PathVariable Integer id) throws PersonNotFoundException {
    return personService.getPerson(id);
  }

  @PutMapping("/update/person/id/{id}")
  public void updatePerson(@PathVariable Integer id, @RequestBody PersonDto personDto)
      throws PersonNotFoundException {
    personService.updatePerson(id, personDto);
  }

  @DeleteMapping("/delete/person/id/{id}")
  public String deletePerson(@PathVariable Integer id) throws PersonNotFoundException {
    personService.deletePerson(id);
    return "Deleted person with id: " + id;
  }
}
