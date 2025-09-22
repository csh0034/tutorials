// JEP 513: Flexible Constructor Bodies
package com.ask.jdk25;

public class JEP513 extends Person {
  final String name;

  JEP513(String name, int age) {
    if (age < 18 || age > 67) {
      throw new IllegalArgumentException("Age must be between 18 and 67");
    }

    super(age);
    this.name = name;
  }

  static void main() {
    var emp = new JEP513("Alice", 35);
    System.out.println("Person age set: " + emp.age);
  }
}

class Person {
  final int age;

  Person(int age) {
    this.age = age;
  }
}
