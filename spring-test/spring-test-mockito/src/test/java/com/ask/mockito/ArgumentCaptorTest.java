package com.ask.mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.internal.verification.VerificationModeFactory.only;

import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ArgumentCaptorTest {

  @Mock
  private Map<String, String> map;

  @Mock
  private Set<String> set;

  @Captor
  private ArgumentCaptor<String> argumentCaptor;

  @Test
  void map() {
    // given
    String key = "name";
    String value = "둘리";

    // when
    map.put(key, value);

    // then
    then(map).should(only()).put(any(), argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isEqualTo(value);
  }

  @Test
  void set() {
    // given
    String value = "둘리";

    // when
    set.add(value);

    // then
    then(set).should(only()).add(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isEqualTo(value);
  }

}
