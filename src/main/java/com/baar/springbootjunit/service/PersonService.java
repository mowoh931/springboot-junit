package com.baar.springbootjunit.service;

import com.baar.springbootjunit.dto.PersonDto;
import com.baar.springbootjunit.exception.PersonExistsException;
import com.baar.springbootjunit.model.Person;

public interface PersonService {


    Person createPerson(PersonDto personDto) throws PersonExistsException;
}
