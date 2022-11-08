package com.ask.springjpacore.web;

import com.ask.springjpacore.entity.Company;
import com.ask.springjpacore.service.CompanyService;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyService companyService;
  private final TransactionTemplate transactionTemplate;

  @PersistenceUnit
  private EntityManagerFactory entityManagerFactory;

  @PersistenceContext
  private EntityManager entityManager;

  @PersistenceContext(type = PersistenceContextType.EXTENDED)
  private EntityManager entityManagerExtended;

  @PostMapping("/add")
  public String add() {
    Company company = Company.create("company-" + System.currentTimeMillis());
    companyService.save(company);
    return company.getId();
  }

  @PostMapping("/add-emf")
  public String addEmf(@RequestParam(defaultValue = "false") boolean shouldClose) {
    Company company = Company.create("company-" + System.currentTimeMillis());
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    entityManager.getTransaction().begin();
    entityManager.persist(company);
    entityManager.getTransaction().commit();

    if (shouldClose) {
      entityManager.close();
    }

    return company.getId();
  }

  @PostMapping("/add-em")
  public String addEm(@RequestParam(defaultValue = "false") boolean useExtended) {
    Company company = Company.create("company-" + System.currentTimeMillis());

    transactionTemplate.executeWithoutResult((status) -> {
      log.info("in transactionTemplate: {}", TransactionSynchronizationManager.isActualTransactionActive());

      if (useExtended) {
        entityManagerExtended.persist(company);
      } else {
        entityManager.persist(company);
      }
    });

    return company.getId();
  }


  @PostMapping("/update/{companyId}")
  public String update(@PathVariable String companyId) {
    Company company = companyService.findById(companyId)
        .orElseThrow(() -> new IllegalArgumentException("not found"));

    company.updateName("company-" + System.currentTimeMillis());

    companyService.save(company);

    return "success";
  }

}
