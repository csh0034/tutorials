package com.ask.springjpacore.entity;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.codec.digest.DigestUtils;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class Password {

  @Column(name = "password", nullable = false)
  private String value;

  @Column(name = "password_expiration_date")
  private LocalDateTime expirationDate;

  @Column(name = "password_failed_count", nullable = false)
  private int failedCount;

  @Column(name = "password_update_dt", nullable = false)
  private LocalDateTime updateDt;

  public static Password create(String value) {
    Password password = new Password();
    password.value = encodePassword(value);
    password.expirationDate = extendExpirationDate();
    return password;
  }

  private static String encodePassword(String value) {
    return DigestUtils.sha256Hex(value);
  }

  private static LocalDateTime extendExpirationDate() {
    return LocalDateTime.now()
        .truncatedTo(ChronoUnit.DAYS)
        .plusMonths(1)
        .plusDays(1)
        .minusSeconds(1);
  }

  public void changePassword(String oldPassword, String newPassword) {
    if (matches(oldPassword)) {
      this.value = encodePassword(newPassword);
      this.expirationDate = extendExpirationDate();
    }
  }

  private boolean matches(String rawPassword) {
    return this.value.equals(encodePassword(rawPassword));
  }

  @PrePersist
  public void prePersist() {
    this.updateDt = LocalDateTime.now();
  }

}
