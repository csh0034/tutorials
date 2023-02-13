package com.ask.quickperf;

import com.ask.quickperf.entity.Coach;
import com.ask.quickperf.entity.Locker;
import com.ask.quickperf.entity.Player;
import com.ask.quickperf.entity.Room;
import com.ask.quickperf.entity.Team;
import com.ask.quickperf.entity.company.Company;
import com.ask.quickperf.entity.company.CompanyOption;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDB {

  private final InitService initService;

  @PostConstruct
  public void init() {
    initService.init();
  }

  @Service
  @RequiredArgsConstructor
  public static class InitService {

    private final EntityManager em;

    @Transactional
    public void init() {
      Team team = new Team();

      Player player = new Player();
      player.setTeam(team);

      Room room = new Room();
      room.setTeam(team);

      Coach coach = new Coach();
      coach.setPlayer(player);

      Locker locker = new Locker();
      locker.setPlayer(player);

      em.persist(team);
      em.persist(player);
      em.persist(room);
      em.persist(coach);
      em.persist(locker);

      Company company = new Company();
      company.setName("company");
      em.persist(company);

      CompanyOption companyOption = new CompanyOption();
      companyOption.setId(company.getId());
      companyOption.setOption1("option1");
      companyOption.setCompany(company);
      em.persist(companyOption);

      company.setCompanyOption(companyOption);
    }
  }
}
