package com.ask.javacore;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.javacore.common.BaseTest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class SerializeTest extends BaseTest {

  private static final User USER = new User("ASk");
  private static final String RESULT = "rO0ABXNyACNjb20uYXNrLmphdmFjb3JlLlNlcmlhbGl6ZVRlc3QkVXNlcs7gu1KugoBkAgABTAAEbmFtZXQAEkxqYXZhL2xhbmcvU3RyaW5nO3hwdAADQVNr";

  @Test
  void serialize() {
    // when
    String result;

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
        oos.writeObject(USER);
        byte[] bytes = baos.toByteArray();
        result = Base64.getEncoder().encodeToString(bytes);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // then
    assertThat(result).isEqualTo(RESULT);
  }

  @Test
  void deserialize() {
    // given
    byte[] bytes = Base64.getDecoder().decode(RESULT);

    // when
    User user;

    try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes)) {
      try (ObjectInputStream ois = new ObjectInputStream(bais)) {
        user = (User) ois.readObject();
      }
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    // then
    assertThat(user).isEqualTo(USER);
  }

  public static class User implements Serializable {

    private static final long serialVersionUID = -3539623343323709340L;

    private final String name;

    public User(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      User user = (User) o;

      return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(name);
    }

  }

}
