package com.ask.springcore.constants;

import static java.util.stream.Collectors.toSet;

import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.Constants;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstantUtils {

  public static Set<String> extractNames(Class<?> target) {
    Constants constants = new Constants(target);
    return constants.getNames(null);
  }

  @SuppressWarnings("unchecked")
  public static <T> Set<T> extractValues(Class<?> target) {
    Constants constants = new Constants(target);
    Set<Object> values = constants.getValues(null);
    return values.stream()
        .map(value -> ((T) value))
        .collect(toSet());
  }

}
