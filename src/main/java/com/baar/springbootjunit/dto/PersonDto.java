package com.baar.springbootjunit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonDto {
  Integer id;
  String name;
  String city;

  public PersonDto(String name, String city) {
    this.name = name;
    this.city = city;
  }
}
