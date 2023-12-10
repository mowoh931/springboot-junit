package com.baar.springbootjunit.repository;

import com.baar.springbootjunit.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {}
