package com.ask.hibernatejasypt.entity;

import static lombok.AccessLevel.PROTECTED;

import com.ask.hibernatejasypt.config.JasyptConfig;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.jasypt.hibernate5.type.EncryptedStringType;
import org.jasypt.hibernate5.type.ParameterNaming;

@Entity
@Table(name = "tb_user")
@TypeDef(
    name = JasyptConfig.ENCRYPTOR_NAME,
    typeClass = EncryptedStringType.class,
    parameters = {
        @Parameter(name = ParameterNaming.ENCRYPTOR_NAME, value = JasyptConfig.ENCRYPTOR_REGISTERED_NAME)
    }
)
@NoArgsConstructor(access = PROTECTED)
@Getter
@ToString
public class User {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "user_id")
  private String id;

  private String name;

  @Type(type = JasyptConfig.ENCRYPTOR_NAME)
  private String data;

  public static User create(String name, String data) {
    User user = new User();
    user.name = name;
    user.data = data;
    return user;
  }

}
