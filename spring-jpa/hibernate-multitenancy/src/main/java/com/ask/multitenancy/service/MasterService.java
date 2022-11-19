package com.ask.multitenancy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MasterService {

  @Transactional
  public void nested() {
    log.info("master.test");
  }

}
