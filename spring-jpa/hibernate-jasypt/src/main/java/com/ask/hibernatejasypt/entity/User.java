package com.ask.hibernatejasypt.entity;

import com.ask.hibernatejasypt.config.JasyptConfig;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.jasypt.hibernate5.type.EncryptedDateAsStringType;
import org.jasypt.hibernate5.type.EncryptedIntegerAsStringType;
import org.jasypt.hibernate5.type.EncryptedStringType;
import org.jasypt.hibernate5.type.ParameterNaming;

@Entity
@Table(name = "tb_user")
@TypeDefs({
    @TypeDef(
        name = "encryptedString",
        typeClass = EncryptedStringType.class,
        parameters = {
            @Parameter(name = ParameterNaming.ENCRYPTOR_NAME, value = JasyptConfig.ENCRYPTOR_REGISTERED_NAME)
        }
    ),
    @TypeDef(
        name = "encryptedIntegerAsString",
        typeClass = EncryptedIntegerAsStringType.class,
        parameters = {
            @Parameter(name = ParameterNaming.ENCRYPTOR_NAME, value = JasyptConfig.ENCRYPTOR_REGISTERED_NAME)
        }
    ),
    @TypeDef(
        name = "encryptedDateAsString",
        typeClass = EncryptedDateAsStringType.class,
        parameters = {
            @Parameter(name = ParameterNaming.ENCRYPTOR_NAME, value = JasyptConfig.ENCRYPTOR_REGISTERED_NAME)
        }
    )
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class User {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "user_id")
  private String id;

  private String name;

  @Type(type = "encryptedString")
  private String data;

  @Type(type = "encryptedIntegerAsString")
  private Integer count;

  @Type(type = "encryptedDateAsString")
  private Date createdDt;

  public static User create(String name, String data) {
    User user = new User();
    user.name = name;
    user.data = data;
    user.count = 1000000;
    user.createdDt = new Date();
    return user;
  }

}
