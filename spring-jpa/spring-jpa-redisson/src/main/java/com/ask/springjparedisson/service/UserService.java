package com.ask.springjparedisson.service;

import com.ask.springjparedisson.AppRunner;
import com.ask.springjparedisson.entity.Company;
import com.ask.springjparedisson.entity.User;
import com.ask.springjparedisson.repository.CompanyRepository;
import com.ask.springjparedisson.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final CompanyRepository companyRepository;

  public String add(String name) {
    Company company = getStoredCompany();
    User user = User.create(name, name + "1234", company);

    userRepository.save(user);

    return user.getId();
  }

  @Transactional(readOnly = true)
  public User find(String id) {
    return userRepository.findById(id).orElseThrow(() -> new IllegalStateException("user not found"));
  }

  @Transactional(readOnly = true)
  public Company getStoredCompany() {
    return companyRepository.findById(AppRunner.STORED_NOTICE_ID)
        .orElseThrow(() -> new IllegalStateException("company not found"));
  }
}
