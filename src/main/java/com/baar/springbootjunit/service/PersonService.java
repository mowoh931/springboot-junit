package com.baar.springbootjunit.service;

import com.baar.springbootjunit.dto.PersonDto;
import com.baar.springbootjunit.exception.PersonExistsException;
import com.baar.springbootjunit.exception.PersonNotFoundException;
import com.baar.springbootjunit.model.Person;
import java.util.List;

public interface PersonService {
  Person createPerson(PersonDto personDto) throws PersonExistsException;

  List<PersonDto> getPersons();

  PersonDto getPerson(Integer id) throws PersonNotFoundException;

  PersonDto findByNameAndCity(String name, String city) throws PersonNotFoundException;
  void updatePerson(Integer id, PersonDto personDto) throws PersonNotFoundException;

  void deletePerson(Integer id) throws PersonNotFoundException;
}
