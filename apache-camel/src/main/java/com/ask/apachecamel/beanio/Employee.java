package com.ask.apachecamel.beanio;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Employee {

  private String firstName;
  private String lastName;
  private String title;
  private int salary;
  private Date hireDate;

}
