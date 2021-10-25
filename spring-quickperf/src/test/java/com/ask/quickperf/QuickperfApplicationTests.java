package com.ask.quickperf;

import com.ask.quickperf.entity.Coach;
import com.ask.quickperf.entity.Locker;
import com.ask.quickperf.entity.Player;
import com.ask.quickperf.entity.Room;
import com.ask.quickperf.entity.Team;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.quickperf.junit5.QuickPerfTestExtension;
import org.quickperf.sql.annotation.ExpectSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(QuickPerfTestExtension.class)
@Transactional
@Slf4j
class QuickperfApplicationTests {

  @Autowired
  EntityManager em;

  @ExpectSelect(4)
  @Test
  void findTeam() {
    em.find(Team.class, 1L);
  }

  @Test
  void findPlayer() {
    em.find(Player.class, 1L);
  }

  @Test
  void findRoom() {
    em.find(Room.class, 1L);
  }

  @Test
  void findCoach() {
    em.find(Coach.class, 1L);
  }

  @Test
  void findLocker() {
    em.find(Locker.class, 1L);
  }

  @Test
  void findTeamWithJpql() {
    em.createQuery("select t from Team t where t.id=:id", Team.class)
        .setParameter("id", 1L).
        getSingleResult();
  }

  @ExpectSelect
  @Test
  void findTeamWithJpqlFetchJoin() {
    em.createQuery("select t from Team t join fetch t.player p join fetch p.coach join fetch p.locker where t.id=:id",
            Team.class)
        .setParameter("id", 1L)
        .getSingleResult();
  }

}
