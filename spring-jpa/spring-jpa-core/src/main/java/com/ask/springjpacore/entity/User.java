package com.ask.springjpacore.entity;

import static lombok.AccessLevel.PROTECTED;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_user")
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class User {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "user_id")
  private String id;

  @NotNull
  private String name;

  @Embedded
  private Password password;

  @ElementCollection
  @CollectionTable(
      name = "address",
      joinColumns = @JoinColumn(name = "user_id")
  )
  private List<Address> addresses = new ArrayList<>();

  public static User create(String name, Password password) {
    User user = new User();
    user.name = name;
    user.password = password;
    return user;
  }

  public void addAddress(Address address) {
    addresses.add(address);
  }

}
