package com.baar.springbootjunit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
@Entity
public class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Integer id;
  String name;
  String city;


}
