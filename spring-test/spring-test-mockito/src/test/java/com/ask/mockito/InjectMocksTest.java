package com.ask.mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.ask.mockito.repository.SampleRepository;
import com.ask.mockito.service.SampleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InjectMocksTest {

  @InjectMocks
  private SampleService sampleService;

  @Spy
  private SampleRepository sampleRepository;

  @Test
  void save() {
    // given
    String value = "value..";

    given(sampleRepository.get(any())).willReturn(value);

    // when
    String result = sampleService.get(any());

    // then
    assertThat(result).isEqualTo(value);
  }

}
