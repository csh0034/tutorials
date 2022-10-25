package com.ask.springjpacore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ask.springjpacore.entity.Mission;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@Slf4j
class MissionRepositoryTest {

  @Autowired
  private MissionRepository missionRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @Test
  void create() {
    // given
    Mission mission = Mission.create("샘플 미션");

    // when
    missionRepository.saveAndFlush(mission);

    // then
    assertThat(mission.getId()).isNotNull();
  }

  @Test
  void delete() {
    // given
    Mission mission = Mission.create("샘플 미션");
    missionRepository.saveAndFlush(mission);

    // when
    missionRepository.delete(mission);
    testEntityManager.flush();

    // then
    assertThat(missionRepository.findById(mission.getId())).isEmpty();
  }

}
