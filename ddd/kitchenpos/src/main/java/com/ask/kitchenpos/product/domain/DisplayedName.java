package com.ask.kitchenpos.product.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DisplayedName {

  @Column(name = "name", nullable = false)
  private String value;

  public DisplayedName(String value, Profanities profanities) {
    validate(value, profanities);
    this.value = value;
  }

  private void validate(String value, Profanities profanities) {
    if (value == null) {
      throw new IllegalArgumentException("name 은 null 일 수 없습니다.");
    }
    if (profanities.contains(value)) {
      throw new IllegalArgumentException("name 에 비속어가 포함될수 없습니다.");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DisplayedName that = (DisplayedName) o;

    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

}
