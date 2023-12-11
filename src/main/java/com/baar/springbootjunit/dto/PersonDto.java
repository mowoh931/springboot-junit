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
}
