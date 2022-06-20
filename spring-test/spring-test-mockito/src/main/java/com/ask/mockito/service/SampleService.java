package com.ask.mockito.service;

import com.ask.mockito.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SampleService {

  private final SampleRepository sampleRepository;

  public String get(String key) {
    return sampleRepository.get(key);
  }

}
