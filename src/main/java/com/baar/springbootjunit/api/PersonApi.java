package com.baar.springbootjunit.api;

import com.baar.springbootjunit.dto.PersonDto;
import com.baar.springbootjunit.exception.PersonExistsException;
import com.baar.springbootjunit.model.Person;
import com.baar.springbootjunit.service.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/persons")
public class PersonApi {

  @Autowired PersonServiceImpl service;

  @PostMapping("/save")
  public Person createPerson(PersonDto personDto) throws PersonExistsException {
    return service.createPerson(personDto);
  }
}
