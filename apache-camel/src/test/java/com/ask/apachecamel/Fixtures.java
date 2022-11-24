package com.ask.apachecamel;

import com.ask.apachecamel.beanio.Employee;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Fixtures {

  public static Employee employee() {
    Employee employee = new Employee();
    employee.setFirstName("길동");
    employee.setLastName("홍");
    employee.setTitle("Marketing");
    employee.setSalary(60000);
    employee.setHireDate(new Date());
    return employee;
  }

  public static List<Employee> employees() {
    try {
      List<Employee> employees = new ArrayList<>();
      Employee one = new Employee();
      one.setFirstName("길동");
      one.setLastName("홍");
      one.setTitle("Developer");
      one.setSalary(75000);
      one.setHireDate(new SimpleDateFormat("MMddyyyy").parse("10012009"));
      employees.add(one);

      Employee two = new Employee();
      two.setFirstName("Jane");
      two.setLastName("Doe");
      two.setTitle("Architect");
      two.setSalary(80000);
      two.setHireDate(new SimpleDateFormat("MMddyyyy").parse("01152008"));
      employees.add(two);

      return employees;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String stringEmployees() {
    return "길동,홍,Developer,75000,10012009" + System.lineSeparator()
        + "Jane,Doe,Architect,80000,01152008" + System.lineSeparator()
        + "Jon,Anderson,Manager,85000,03182007" + System.lineSeparator();
  }

}
