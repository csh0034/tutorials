package com.ask.springjpajcache.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.springjpajcache.entity.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoleRepositoryTest {

  @Autowired
  private RoleRepository roleRepository;

  @DisplayName("@Immutable 선언시에 엔티티 생성은 동작함.")
  @Test
  void create() {
    // given
    Role adminRole = Role.create("originalName");

    // when
    roleRepository.save(adminRole);

    // then
    assertThat(adminRole.getId()).isNotNull();
  }

  @DisplayName("@Immutable 선언시에 엔티티 변경은 무시됨.")
  @Test
  void update() {
    // given
    String originalName = "originalName";
    String updateName = "updateAdminName";

    Role adminRole = Role.create(originalName);
    roleRepository.saveAndFlush(adminRole);

    // when
    adminRole.updateName(updateName);
    roleRepository.saveAndFlush(adminRole);

    // then
    Role storedRole = roleRepository.findById(adminRole.getId()).orElseThrow(() -> new RuntimeException("엔티티 없음"));
    assertThat(storedRole.getName()).isEqualTo(originalName);
  }
}