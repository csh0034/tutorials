package com.ask.springcore.config.converter;

import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.Assert;

@SuppressWarnings("rawtypes")
public class LenientStringToEnumConverterFactory implements ConverterFactory<String, Enum<?>> {

  @Override
  @SuppressWarnings("unchecked")
  public <E extends Enum<?>> Converter<String, E> getConverter(Class<E> targetType) {
    Class<?> enumType = targetType;
    while (enumType != null && !enumType.isEnum()) {
      enumType = enumType.getSuperclass();
    }
    Assert.notNull(enumType, () -> "The target type " + targetType.getName() + " does not refer to an enum");
    return new LenientToEnumConverter<>((Class<E>) enumType);
  }

  @SuppressWarnings("unchecked")
  private static class LenientToEnumConverter<E extends Enum> implements Converter<String, E> {

    private final Class<E> enumType;
    private Map<String, Enum<? extends Code>> ALL = Collections.emptyMap();

    private LenientToEnumConverter(Class<E> enumType) {
      this.enumType = enumType;

      if (Code.class.isAssignableFrom(enumType)) {
        ALL = Arrays.stream(enumType.getEnumConstants())
            .collect(toMap(o -> ((Code)o).getCode(), o -> o));
      }
    }

    @Override
    public E convert(String source) {
      String value = source.trim();
      if (value.isEmpty()) {
        return null;
      }
      try {
        return (E) Enum.valueOf(this.enumType, value);
      } catch (Exception ex) {
        if (Code.class.isAssignableFrom(enumType)) {
          Enum<? extends Code> result = ALL.get(value);
          if (result != null) {
            return (E) result;
          }
        }
        return findEnum(value);
      }
    }

    private E findEnum(String value) {
      String name = getCanonicalName(value);
      for (E candidate : (Set<E>) EnumSet.allOf(this.enumType)) {
        String candidateName = getCanonicalName(candidate.name());
        if (name.equals(candidateName)) {
          return candidate;
        }
      }
      throw new IllegalArgumentException("No enum constant " + this.enumType.getCanonicalName() + "." + value);
    }

    private String getCanonicalName(String name) {
      StringBuilder canonicalName = new StringBuilder(name.length());
      name.chars().filter(Character::isLetterOrDigit).map(Character::toLowerCase)
          .forEach((c) -> canonicalName.append((char) c));
      return canonicalName.toString();
    }

  }

}
