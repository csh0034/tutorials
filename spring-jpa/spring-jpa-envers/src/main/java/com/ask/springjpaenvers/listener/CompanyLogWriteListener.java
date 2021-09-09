package com.ask.springjpaenvers.listener;

import com.ask.springjpaenvers.entity.Company;
import com.ask.springjpaenvers.entity.CompanyLog;
import com.ask.springjpaenvers.entity.CompanyLog.LogType;
import com.ask.springjpaenvers.service.CompanyLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;

@RequiredArgsConstructor
@Slf4j
public class CompanyLogWriteListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

  private static final long serialVersionUID = 7876932731944094153L;

  private final CompanyLogService companyLogService;

  @Override
  public void onPostInsert(PostInsertEvent event) {
    final Object entity = event.getEntity();

    if (entity instanceof Company) {
      log.info("Company onPostInsert");

      Company company = (Company) entity;

      CompanyLog companyLog = CompanyLog.create(company.getId(), company.getName(), company.getCount(), LogType.CREATE);

      // ActionQueue 에 이벤트 콜백 등록
      event.getSession().getActionQueue().registerProcess((success, session) -> {
        if (success) {
          companyLogService.save(companyLog);
        }
      });
    }
  }

  @Override
  public void onPostUpdate(PostUpdateEvent event) {
    final Object entity = event.getEntity();

    if (entity instanceof Company) {
      log.info("Company onPostUpdate");

      Company company = (Company) entity;

      CompanyLog companyLog = CompanyLog.create(company.getId(), company.getName(), company.getCount(), LogType.UPDATE);

      event.getSession().getActionQueue().registerProcess((success, session) -> {
        if (success) {
          companyLogService.save(companyLog);
        }
      });
    }
  }

  @Override
  public void onPostDelete(PostDeleteEvent event) {
    final Object entity = event.getEntity();

    if (entity instanceof Company) {
      log.info("Company onPostDelete");
    }
  }

  @Override
  public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
    return true;
  }
}
