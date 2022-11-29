package com.ask.apachecamel.beanio;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;
import org.beanio.annotation.Segment;
import org.beanio.builder.Align;

@Record(maxOccurs = 1)
@Getter
@Setter
@ToString
public class User {

  @Field(required = true, length = 10, padding = '-')
  private String name;

  @Field(required = true, length = 3, padding = '-')
  private int age;

  @Field(format = "yyyy-MM-dd HH:mm:ss", length = 19)
  private Date joinedDt;

  @Segment(maxOccurs = 1)
  private Address address;

  @Getter
  @Setter
  @ToString
  public static class Address {

    @Field(align = Align.RIGHT, length = 10, padding = '@', defaultValue = "seoul")
    private String city;

    @Field(required = true, length = 10, minLength = 1, maxLength = 10)
    private String street;

    @Field(required = true, length = 20, minLength = 1, maxLength = 20, padding = '@')
    private String zipcode;

  }

}
